package com.datn.ailms.services.orderService;

import com.datn.ailms.interfaces.order_interface.IPurchaseOrderItemService;
import com.datn.ailms.mapper.PurchaseOrderItemMapper;
import com.datn.ailms.model.dto.request.order.PurchaseOrderItemRequestDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;
import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderItemRepository;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
}
