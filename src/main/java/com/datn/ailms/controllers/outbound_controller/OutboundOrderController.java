package com.datn.ailms.controllers.outbound_controller;

import com.datn.ailms.model.dto.request.inventory.ProductConfirmRequestDto;
import com.datn.ailms.model.dto.request.order.ExportRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;

import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.services.orderService.OutboundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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

    @PostMapping("/{orderId}/scanned")
    public ApiResp<List<ProductDetailResponseDto>> scanned(
            @PathVariable UUID orderId, @RequestBody ProductConfirmRequestDto request){
        var result = _outOrderService.scanned(orderId, request);
        return ApiResp.<List<ProductDetailResponseDto>>builder()
                .result(result)
                .build();
    }

    @PostMapping("/{orderId}/confirm-export")
    public ApiResp<OutboundOrderResponseDto> confirmExport(
            @PathVariable UUID orderId, @RequestBody ExportRequestDto request){
        var result = _outOrderService.confirmExport(orderId, request.getExportedBy());
        return ApiResp.<OutboundOrderResponseDto>builder()
                .result(result)
                .build();
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

    @GetMapping("/{orderId}/serials")
    public ApiResp<List<ProductDetailResponseDto>> getSerials(
            @PathVariable UUID orderId, @Param("sku") String sku){
        var result = _outOrderService.getByOrderIdAndSKU(orderId, sku);
        return ApiResp.<List<ProductDetailResponseDto>>builder()
                .result(result)
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
