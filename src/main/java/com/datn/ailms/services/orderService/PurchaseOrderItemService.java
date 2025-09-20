//package com.datn.ailms.services.orderService;
//
//import com.datn.ailms.exceptions.AppException;
//import com.datn.ailms.exceptions.ErrorCode;
//import com.datn.ailms.interfaces.order_interface.IPurchaseOrderItemService;
//import com.datn.ailms.mapper.ProductDetailMapper;
//import com.datn.ailms.mapper.PurchaseOrderItemMapper;
//import com.datn.ailms.model.dto.request.order.PurchaseOrderItemRequestDto;
//import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
//import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;
//import com.datn.ailms.model.entities.enums.SerialStatus;
//import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
//import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
//import com.datn.ailms.model.entities.product_entities.Product;
//import com.datn.ailms.model.entities.product_entities.ProductDetail;
//import com.datn.ailms.repositories.orderRepo.PurchaseOrderItemRepository;
//import com.datn.ailms.repositories.orderRepo.PurchaseOrderRepository;
//import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
//import com.datn.ailms.repositories.productRepo.ProductRepository;
//import com.datn.ailms.services.topoServices.BinRuleService;
//import jakarta.transaction.Transactional;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class PurchaseOrderItemService implements IPurchaseOrderItemService {
//
//    final PurchaseOrderRepository _orderRepo;
//    final PurchaseOrderItemRepository _itemRepo;
//    final PurchaseOrderItemMapper _itemMapper;
//    final ProductRepository _productRepo;
//    final ProductDetailRepository _detailRepo;
//    final ProductDetailMapper _pdMapper;
//    final BinRuleService _binruleService;
//    final BinRepository _binRepo;
//
//    @Override
//    public PurchaseOrderItemResponseDto addItem(UUID orderId, PurchaseOrderItemRequestDto request) {
//        PurchaseOrder order = _orderRepo.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
//
//        Product product = _productRepo.findById(request.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        PurchaseOrderItem item = new PurchaseOrderItem();
//        item.setPurchaseOrder(order);
//        item.setProduct(product);
//        item.setOrderQuantity(request.getOrderQuantity());
//        item.setScannedQuantity(0);
//
//        item = _itemRepo.save(item);
//        return _itemMapper.toDto(item);
//    }
//
//    @Override
//    public void removeItem(UUID itemId) {
//        _itemRepo.deleteById(itemId);
//    }
//
//    @Override
//    public ProductDetailResponseDto scanSerial(UUID itemId, String serialNumber) {
//        PurchaseOrderItem item = _itemRepo.findById(itemId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        // Normalize serial
//        String normSerial = serialNumber == null ? null : serialNumber.trim();
//
//        if (normSerial == null || normSerial.isEmpty()) {
//            throw new AppException(ErrorCode.INVALID_PARAM);
//        }
//
//        // 1. Check if the serial already exists globally
//        Optional<ProductDetail> existing = _detailRepo.findBySerialNumberIgnoreCase(normSerial);
//        if (existing.isPresent()) {
//            ProductDetail ed = existing.get();
//            if (ed.getPurchaseOrderItem() != null && !ed.getPurchaseOrderItem().getId().equals(itemId)) {
//                // already assigned to a different order item
//                throw new AppException(ErrorCode.SERIAL_ALREADY_SCANNED);
//            }
//            // if exists and belongs to same item -> return dto
//            if (ed.getPurchaseOrderItem() != null && ed.getPurchaseOrderItem().getId().equals(itemId)) {
//                return _pdMapper.toResponse(ed);
//            }
//            // else exists but not assigned yet -> we'll assign below
//        }
//
//        // 2. Ensure we have product master to assign (prefer the product of order item)
//        Product product = item.getProduct();
//        if (product == null) {
//            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
//        }
//
//        ProductDetail detail;
//        if (existing.isPresent()) {
//            detail = existing.get();
//        } else {
//            // create new ProductDetail
//            detail = ProductDetail.builder()
//                    .serialNumber(normSerial)
//                    .product(product)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//        }
//
//        // 3. If detail.product is null, set it (shouldn't happen if orderItem.product provided)
//        if (detail.getProduct() == null) {
//            detail.setProduct(product);
//        }
//
//        // 4. If bin not assigned yet -> find bin via BinRuleService
//        if (detail.getBin() == null) {
//            Bin bin = _binruleService.findBinForSerial(normSerial); // may throw if none -> bubble up
//            // ensure capacity
//            if (bin.getCurrentQty() >= bin.getCapacity()) {
//                throw new AppException(ErrorCode.BIN_FULL);
//            }
//            bin.setCurrentQty(bin.getCurrentQty() + 1);
//            _binRepo.save(bin);
//            detail.setBin(bin);
//        }
//
//        // 5. Assign to order item
//        detail.setPurchaseOrderItem(item);
//        detail.setUpdatedAt(LocalDateTime.now());
//
//        // ✅ set status INBOUND khi scan
//        detail.setStatus(SerialStatus.INBOUND);
//
//        // persist detail
//        ProductDetail saved = _detailRepo.save(detail);
//
//        // 6. Update scannedQuantity on item
//        int scanned = (int) item.getProductDetails().stream()
//                .map(ProductDetail::getSerialNumber)
//                .filter(s -> s != null).count();
//
//        // if saved is new, add to list (ensure in-memory sync)
//        if (item.getProductDetails().stream().noneMatch(d -> d.getId() != null && d.getId().equals(saved.getId()))) {
//            item.getProductDetails().add(saved);
//            scanned = scanned + 1; // adjust
//        }
//
//        item.setScannedQuantity(scanned);
//        _itemRepo.save(item); // save update
//
//        return _pdMapper.toResponse(saved);
//    }
//}
