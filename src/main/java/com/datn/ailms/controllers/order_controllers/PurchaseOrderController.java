package com.datn.ailms.controllers.order_controllers;

import com.datn.ailms.interfaces.order_interface.IPurchaseOrderService;
import com.datn.ailms.model.dto.request.order.PurchaseOrderRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.order.PurchaseOrderResponseDto;
import com.datn.ailms.services.orderService.PurchaseOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderController {

    final PurchaseOrderService _purchaseOrderService;

    @PostMapping
    ApiResp<PurchaseOrderResponseDto> create(@RequestBody PurchaseOrderRequestDto purchaseOrderRequestDto) {
        var result = _purchaseOrderService.create(purchaseOrderRequestDto);
        return ApiResp.<PurchaseOrderResponseDto>builder().result(result).build();
    }

    @GetMapping
    ApiResp<List<PurchaseOrderResponseDto>> getAll(){
        var result = _purchaseOrderService.getAll();
        return ApiResp.<List<PurchaseOrderResponseDto>>builder().result(result).build();
    }

    @GetMapping("/{id}")
    ApiResp<PurchaseOrderResponseDto> getById(@PathVariable UUID id){
        var result = _purchaseOrderService.getById(id);
        return ApiResp.<PurchaseOrderResponseDto>builder().result(result).build();
    }

    @PutMapping("/{id}")
    ApiResp<PurchaseOrderResponseDto> update(@PathVariable UUID id, @RequestBody PurchaseOrderRequestDto purchaseOrderRequestDto){
        var result = _purchaseOrderService.update(id, purchaseOrderRequestDto);
        return ApiResp.<PurchaseOrderResponseDto>builder().result(result).build();
    }
    @DeleteMapping("/{id}")
    ApiResp<PurchaseOrderResponseDto> delete(@PathVariable UUID id){
         _purchaseOrderService.delete(id);
        return ApiResp.<PurchaseOrderResponseDto>builder().build();
    }

}
