package com.datn.ailms.controllers.outbound_controller;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.model.dto.request.order.OutboundOrderItemRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.entities.enums.OrderStatus;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import com.datn.ailms.repositories.orderRepo.OutboundOrderRepository;
import com.datn.ailms.services.orderService.OutboundOrderItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/outbound")
public class OutboundOrderItemController {

    final OutboundOrderItemService _outboundItemService;
    final OutboundOrderRepository _outboundRepo;

    @PutMapping("/{orderId}/items")
    public ApiResp<List<OutboundOrderItemResponseDto>> saveOrUpdateItems(
            @RequestBody OutboundOrderRequestDto request,
            @PathVariable UUID orderId){
//
//        OutboundOrder outOrder = _outboundRepo.findById(orderId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        if(outOrder.getStatus().equals(OrderStatus.CONFIRMED)){
//            throw new AppException(ErrorCode.ORDER_CONFIRMED_CANNOT_BULK_UPDATE);
//        } else if (outOrder.getStatus().equals(OrderStatus.DRAFT)) {
            var result = _outboundItemService.addItem(request,orderId);
            return ApiResp.<List<OutboundOrderItemResponseDto>>builder()
                    .result(result)
                    .build();
//        }
//
//        return ApiResp.<List<OutboundOrderItemResponseDto>>builder()
//                .build();

    }


    @PutMapping("/{orderId}")
    public ApiResp<OutboundOrderItemResponseDto> updateItem(
            @PathVariable UUID orderId,
            @PathVariable UUID warehouseId,
            @RequestBody OutboundOrderItemRequestDto request){
        var result = _outboundItemService.update(request, orderId,warehouseId);
        return ApiResp.<OutboundOrderItemResponseDto>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{orderId}/item/{productId}")
    public ApiResp<Void> deleteItem(@PathVariable UUID orderId, @PathVariable UUID productId){
        _outboundItemService.deleteItem(productId,orderId);
        return ApiResp.<Void>builder()
                .build();
    }
}
