package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.order_interface.IPurchaseOrderItemService;
import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.mapper.PurchaseOrderItemMapper;
import com.datn.ailms.model.dto.request.order.PurchaseOrderItemRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderItemRepository;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import com.datn.ailms.services.topoServices.WarehouseRuleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderItemService implements IPurchaseOrderItemService {

    final PurchaseOrderRepository _orderRepo;
    final PurchaseOrderItemRepository _itemRepo;
    final PurchaseOrderItemMapper _itemMapper;
    final ProductRepository _productRepo;
    final ProductDetailRepository _detailRepo;
    final ProductDetailMapper _pdMapper;
    final WarehouseRuleService _warehouseRuleService;
    final WarehouseRepository _whRepo;
    final UserRepository _userRepo;

    @Override
    public PurchaseOrderItemResponseDto addItem(UUID orderId, PurchaseOrderItemRequestDto request) {
        PurchaseOrder order = _orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));

        Product product = _productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setPurchaseOrder(order);
        item.setProduct(product);
        item.setOrderQuantity(request.getOrderQuantity());
        item.setScannedQuantity(0);

        item = _itemRepo.save(item);
        return _itemMapper.toDto(item);
    }

    @Override
    public void removeItem(UUID itemId) {
        _itemRepo.deleteById(itemId);
    }

    @Override
    public ProductDetailResponseDto scanSerial(UUID itemId, String serialNumber,UUID userId) {
        PurchaseOrderItem item = _itemRepo.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        String normSerial = serialNumber == null ? null : serialNumber.trim();
        if (normSerial == null || normSerial.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_PARAM);
        }

        ProductDetail detail = prepareProductDetail(item, normSerial, userId);
        ProductDetail saved = _detailRepo.save(detail);

        if (item.getProductDetails().stream().noneMatch(d -> d.getId().equals(saved.getId()))) {
            item.getProductDetails().add(saved);
        }

        updateScannedQuantity(item);

        return _pdMapper.toResponse(saved);
    }

    @Override
    public ProductDetail prepareProductDetail(PurchaseOrderItem item, String normSerial, UUID userId) {
        Optional<ProductDetail> existing = _detailRepo.findBySerialNumberIgnoreCase(normSerial);

        if (existing.isPresent()) {
            ProductDetail ed = existing.get();
            // Nếu serial đã gắn vào item khác thì chặn
            if (ed.getPurchaseOrderItem() != null && !ed.getPurchaseOrderItem().getId().equals(item.getId())) {
                throw new AppException(ErrorCode.SERIAL_ALREADY_SCANNED);
            }

            // Nếu serial đã gắn đúng item hiện tại rồi thì cũng chặn (không cho quét lại)
            if (ed.getPurchaseOrderItem() != null && ed.getPurchaseOrderItem().getId().equals(item.getId())) {
                throw new AppException(ErrorCode.SERIAL_ALREADY_SCANNED);
            }

            // 🔥 Bổ sung quan hệ nếu thiếu
            if (ed.getProduct() == null) {
                ed.setProduct(item.getProduct());
            }
            if (ed.getWarehouse() == null) {
                Warehouse wh = _warehouseRuleService.findWarehouseForSerial(normSerial);
                if (wh.getCurrentQuantity() >= wh.getCapacity()) {
                    throw new AppException(ErrorCode.BIN_FULL);
                }
                wh.setCurrentQuantity(wh.getCurrentQuantity() + 1);
                _whRepo.save(wh);
                ed.setWarehouse(wh);
            }
            if (ed.getPurchaseOrderItem() == null) {
                ed.setPurchaseOrderItem(item);
            }
            if (ed.getScannedBy() == null) {
                User currentUser = _userRepo.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                ed.setScannedBy(currentUser);
            }

            ed.setStatus(SerialStatus.INBOUND);
            ed.setUpdatedAt(LocalDateTime.now());
            return ed;
        }

        // ===== Nếu chưa tồn tại thì tạo mới =====
        Product product = Optional.ofNullable(item.getProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductDetail detail = ProductDetail.builder()
                .serialNumber(normSerial)
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();

        Warehouse wh = _warehouseRuleService.findWarehouseForSerial(normSerial);
        if (wh.getCurrentQuantity() >= wh.getCapacity()) {
            throw new AppException(ErrorCode.BIN_FULL);
        }
        wh.setCurrentQuantity(wh.getCurrentQuantity() + 1);
        _whRepo.save(wh);
        detail.setWarehouse(wh);

        User currentUser = _userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        detail.setScannedBy(currentUser);
        detail.setStatus(SerialStatus.INBOUND);
        detail.setPurchaseOrderItem(item);
        detail.setUpdatedAt(LocalDateTime.now());

        return detail;
    }


    @Override
    public void updateScannedQuantity(PurchaseOrderItem item) {
        int scanned = (int) item.getProductDetails().stream() .filter(d -> d.getSerialNumber() != null) .count();
        item.setScannedQuantity(scanned);
        _itemRepo.save(item);
    }
}
