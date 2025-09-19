package com.datn.ailms.controllers.order_controllers;

import com.datn.ailms.model.dto.request.order.PurchaseOrderItemRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;
import com.datn.ailms.services.orderService.PurchaseOrderItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchase-orders-items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderItemController {

    final PurchaseOrderItemService _itemService;

    @PostMapping("/{orderId}")
    ApiResp<PurchaseOrderItemResponseDto> addItem(
            @PathVariable UUID orderId,
            @RequestBody PurchaseOrderItemRequestDto requestDto
    ){
        var result = _itemService.addItem(orderId, requestDto);
        return ApiResp.<PurchaseOrderItemResponseDto>builder().result(result).build();
    }

    @DeleteMapping("/{itemId}")
    ApiResp<Void> removeItem(@PathVariable UUID itemId){
        _itemService.removeItem(itemId);
        return ApiResp.<Void>builder().build();
    }

}
