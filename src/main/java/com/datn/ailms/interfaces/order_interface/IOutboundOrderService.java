package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.OutboundOrderFilter;
import com.datn.ailms.model.dto.request.inventory.ProductConfirmRequestDto;
import com.datn.ailms.model.dto.request.order.CancelRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IOutboundOrderService {
    Page<OutboundOrderResponseDto> search(OutboundOrderFilter f, Pageable pageable);
    OutboundOrderResponseDto create (OutboundOrderRequestDto request);
    OutboundOrderResponseDto getById (UUID id);
    OutboundOrderResponseDto getByCode (String code);
//    Page<OutboundOrderResponseDto> getAll(Pageable pageable);
    OutboundOrderResponseDto update(OutboundOrderRequestDto request, UUID id);
    void cancelOutbound(UUID orderId, CancelRequestDto request);
    void confirmCancel(UUID orderId);
    void rejectCancel(UUID orderId);
    OutboundOrderResponseDto confirmOrder(OutboundOrderRequestDto request,UUID orderId);
    OutboundOrderResponseDto confirmExport(UUID orderId, UUID userId);
//    List<OutboundOrderResponseDto> getAllByStatus(String status);
    List<OutboundOrderResponseDto> getAllByProductId(UUID productId);
    Page<ProductDetailResponseDto> getByOrderIdAndSKU(UUID orderId, String sku, Pageable pageable);
    List<ProductDetailResponseDto> scanned(UUID orderId, ProductConfirmRequestDto request);
}
