package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.request.order.OutboundOrderItemRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.ProductDetailSerialDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IOutboundOrderItemService {
    List<OutboundOrderItemResponseDto> addItem(OutboundOrderRequestDto request, UUID outboundOrder);
    OutboundOrderItemResponseDto update(OutboundOrderItemRequestDto request, UUID outboundOrderId);
    void removeItem(Set<UUID> productIds, Map<UUID, OutboundOrderItem> existingItemMap);
//    OutboundOrderItemResponseDto addSingleItem (OutboundOrderItemRequestDto request, UUID orderId);
    void deleteItem (UUID productId, UUID outOrderId);

    List<OutboundOrderItemResponseDto> getItemsByOrderId(UUID outboundOrderId);
}
