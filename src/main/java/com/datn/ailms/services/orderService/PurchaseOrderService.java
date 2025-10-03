package com.datn.ailms.services.orderService;


import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.order_interface.IPurchaseOrderService;
import com.datn.ailms.mapper.PurchaseOrderMapper;
import com.datn.ailms.model.dto.request.order.PurchaseOrderRequestDto;
import com.datn.ailms.model.dto.response.ProductDetailSerialDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderItemRepository;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderService implements IPurchaseOrderService {

    final PurchaseOrderRepository _orderRepo;
    final PurchaseOrderMapper _orderMapper;
    final ProductRepository _productRepository;
    final UserRepository _userRepository;
    final ProductDetailRepository _detailRepo;
    final PurchaseOrderItemRepository purchaseOrderItemRepository;
    final WarehouseRepository _warehouseRepo;
    @Override
    public PurchaseOrderResponseDto create(PurchaseOrderRequestDto request) {
        PurchaseOrder order = _orderMapper.toEntity(request);
        // Nếu entity đã dùng @GeneratedValue thì bỏ dòng dưới
        User user = _userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getCreatedBy()));
        order.setCreatedBy(user);

        // Set warehouse
        if (request.getWarehouseId() == null) {
            throw new RuntimeException("Warehouse is required for Purchase Order");
        }
        order.setWarehouse(
                _warehouseRepo.findById(request.getWarehouseId())
                        .orElseThrow(() -> new RuntimeException("Warehouse not found: " + request.getWarehouseId()))
        );

        // Gắn quan hệ 2 chiều: mỗi item phải biết order cha
        if (order.getItems() != null) {
            order.getItems().forEach(item -> {
                item.setPurchaseOrder(order);

                // Lấy productId ra
                UUID productId = item.getProduct().getId();

                // Fetch product từ DB
                Product product = _productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

                // Gán lại product đầy đủ
                item.setProduct(product);
            });
        }
        order.setStatus("IN_BOUND");
        PurchaseOrder savedOrder = _orderRepo.save(order);
        return _orderMapper.toDto(savedOrder);
    }

    @Override
    public PurchaseOrderResponseDto getById(UUID id) {
        PurchaseOrder order = _orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
        return _orderMapper.toDto(order);
    }

    @Override
    public List<PurchaseOrderResponseDto> getAll() {
        return _orderMapper.toResponseList(_orderRepo.findAll());
    }

    @Override
    public PurchaseOrderResponseDto update(UUID id, PurchaseOrderRequestDto request) {
        PurchaseOrder existing = _orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
        // MapStruct có thể hỗ trợ update nếu bạn khai báo @MappingTarget
        existing.setCode(request.getCode());
        existing.setSupplier(request.getSupplier());
        existing.setStatus(request.getStatus());
        existing.setCreatedAt(request.getCreatedAt());

        // Update warehouse nếu có
        if (request.getWarehouseId() != null) {
            existing.setWarehouse(
                    _warehouseRepo.findById(request.getWarehouseId())
                            .orElseThrow(() -> new RuntimeException("Warehouse not found: " + request.getWarehouseId()))
            );
        }
        // Items update có thể phức tạp → cần xử lý thêm
        return _orderMapper.toDto(_orderRepo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        _orderRepo.deleteById(id);
    }

    @Override
    public void completeItem(UUID orderId, UUID userId) {
        PurchaseOrder order = _orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // check user tồn tại
        _userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var warehouse = order.getWarehouse();
        AtomicInteger added = new AtomicInteger(0);

        // cập nhật tất cả ProductDetail về IN_STOCK
        order.getItems().forEach(item -> {
            item.getProductDetails().forEach(detail -> {
                if (detail.getStatus() == SerialStatus.INBOUND) {
                    detail.setStatus(SerialStatus.IN_WAREHOUSE);
                    detail.setUpdatedAt(LocalDateTime.now());
                    _detailRepo.save(detail);
                    added.incrementAndGet();
                }
            });
        });

        warehouse.setCurrentQuantity(warehouse.getCurrentQuantity() + added.get());
        _warehouseRepo.save(warehouse);

//        order.setCreatedAt(new LocalDate());
        order.setStatus("COMPLETED");
        _orderRepo.save(order);
    }

    @Override
    public List<ProductDetailSerialDto> getSerials(UUID orderId, String sku) {
// Lấy tất cả item của đơn hàng
        List<PurchaseOrderItem> items = purchaseOrderItemRepository.findByPurchaseOrderId(orderId);

        return items.stream()
                .flatMap(item -> item.getProductDetails().stream()
                        .map(pd -> {
                            ProductDetailSerialDto dto = new ProductDetailSerialDto();
                            dto.setProductId(pd.getProduct().getId());
                            dto.setSerialNumber(pd.getSerialNumber());
                            dto.setSku(pd.getProduct().getSku());
                            dto.setStatus(pd.getStatus());
                            //dto.set`pd.getBinId() != null ? pd.getBinId().getId().toString() : null);
                            return dto;
                        })
                )
                .toList();
    }

}
