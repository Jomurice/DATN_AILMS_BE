package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.request.order.PurchaseOrderItemRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;

import java.util.UUID;

public interface IPurchaseOrderItemService {
    PurchaseOrderItemResponseDto addItem(UUID orderId, PurchaseOrderItemRequestDto request);
    void removeItem(UUID itemId);
    ProductDetailResponseDto scanSerial(UUID itemId, String serialNumber);
}
