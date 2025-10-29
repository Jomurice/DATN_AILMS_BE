package com.datn.ailms.controllers.outbound_controller;

import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;

import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.services.orderService.OutboundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/outbound")
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
}
