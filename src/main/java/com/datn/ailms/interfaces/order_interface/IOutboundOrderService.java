package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;

import java.util.List;
import java.util.UUID;

public interface IOutboundOrderService {
    OutboundOrderResponseDto create (OutboundOrderRequestDto request);
    OutboundOrderResponseDto getById (UUID id);
    List<OutboundOrderResponseDto> getAll();
    OutboundOrderResponseDto update(OutboundOrderRequestDto request, UUID id);
    OutboundOrderResponseDto confirmOrder(OutboundOrderRequestDto request,UUID orderId);
    List<OutboundOrderResponseDto> getAllByStatus(String status);
    List<OutboundOrderResponseDto> getAllByProductId(UUID productId);
}
