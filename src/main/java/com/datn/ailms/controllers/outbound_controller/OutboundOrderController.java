package com.datn.ailms.controllers.outbound_controller;

import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;

import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.services.orderService.OutboundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/outbound-orders")
public class OutboundOrderController {

    final OutboundOrderService _outOrderService;


    @GetMapping
    public ApiResp<List<OutboundOrderResponseDto>> getAll(){
        return ApiResp.<List<OutboundOrderResponseDto>>builder().result(_outOrderService.getAll()).build();
    }

    @PostMapping
    public ApiResp<OutboundOrderResponseDto> create(@RequestBody OutboundOrderRequestDto requestDto){
        var result = _outOrderService.create(requestDto);
        return ApiResp.<OutboundOrderResponseDto>builder().result(result).build();
    }

    @PatchMapping("/{orderId}/confirm")
    public ApiResp<OutboundOrderResponseDto> confirmOrder(
            @RequestBody OutboundOrderRequestDto request, @PathVariable UUID orderId){

        var result = _outOrderService.confirmOrder(request,orderId);
        return ApiResp.<OutboundOrderResponseDto>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResp<OutboundOrderResponseDto> getById(@PathVariable UUID id){
        return ApiResp.<OutboundOrderResponseDto>builder()
                .code(0)
                .message("success")
                .result(_outOrderService.getById(id))
                .build();
    }

    @GetMapping("/status/{status}")
    public ApiResp<List<OutboundOrderResponseDto>> getAllByStatus(@PathVariable String status){
        return ApiResp.<List<OutboundOrderResponseDto>>builder()
                .code(0)
                .message("success")
                .result(_outOrderService.getAllByStatus(status))
                .build();
    }

    @GetMapping("/product/{productId}")
    public ApiResp<List<OutboundOrderResponseDto>> getAllByProductId(@PathVariable UUID product){
        return ApiResp.<List<OutboundOrderResponseDto>>builder()
                .code(0)
                .message("success")
                .result(_outOrderService.getAllByProductId(product))
                .build();
    }

    @DeleteMapping("/cleanup-draft")
    public ApiResp<String> cleanupDraftOrders() {
        _outOrderService.deleteExpiredDraftOrders();
        return ApiResp.<String>builder()
                .result("Cleanup completed")
                .build();
    }

}
