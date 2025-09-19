package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.request.order.PurchaseOrderRequestDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderResponseDto;

import java.util.List;
import java.util.UUID;

public interface IPurchaseOrderService {
    PurchaseOrderResponseDto create(PurchaseOrderRequestDto request);
    PurchaseOrderResponseDto getById(UUID id);
    List<PurchaseOrderResponseDto> getAll();
    PurchaseOrderResponseDto update(UUID id, PurchaseOrderRequestDto request);
    void delete(UUID id);
}
