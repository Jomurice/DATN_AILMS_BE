package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.request.order.OutboundOrderItemRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;

import java.util.List;
import java.util.UUID;

public interface IOutboundOrderItemService {
    List<OutboundOrderItemResponseDto> addItem(List<OutboundOrderItemRequestDto> request, UUID outboundOrder);
    List<OutboundOrderItemResponseDto> update(OutboundOrderItemRequestDto request, UUID id);
    void removeItem(UUID itemId);
}
