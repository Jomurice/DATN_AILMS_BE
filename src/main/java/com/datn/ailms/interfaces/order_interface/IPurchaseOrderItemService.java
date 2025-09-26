package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.request.order.PurchaseOrderItemRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;

import java.util.UUID;

public interface IPurchaseOrderItemService {
    PurchaseOrderItemResponseDto addItem(UUID orderId, PurchaseOrderItemRequestDto request);
    void removeItem(UUID itemId);
    ProductDetailResponseDto scanSerial(UUID itemId, String serialNumber,UUID userId);
    ProductDetail prepareProductDetail(PurchaseOrderItem item, String normSerial, UUID userId);
    void updateScannedQuantity(PurchaseOrderItem item);
}
