package com.datn.ailms.controllers.topocontrollers;


import com.datn.ailms.mapper.WarehouseRuleMapper;
import com.datn.ailms.model.dto.request.BinRuleRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.WarehouseRuleRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseRuleResponseDto;
import com.datn.ailms.services.topoServices.WarehouseRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/warehouse-rules")
@RequiredArgsConstructor
public class WarehouseRuleController {

    private final WarehouseRuleService _warehouseRuleService;
    @PostMapping
    public ApiResp<WarehouseRuleResponseDto> create(@RequestBody WarehouseRuleRequestDto request) {
        WarehouseRuleResponseDto result = _warehouseRuleService.createRule(request);
        return ApiResp.<WarehouseRuleResponseDto>builder().result(result).build();
    }

    @PutMapping("/{id}")
    public ApiResp<WarehouseRuleResponseDto> update(@PathVariable UUID id,
                                                    @RequestBody WarehouseRuleRequestDto request) {
        WarehouseRuleResponseDto result = _warehouseRuleService.updateRule(id, request);
        return ApiResp.<WarehouseRuleResponseDto>builder().result(result).build();
    }

    @DeleteMapping("/{id}")
    public ApiResp<String> delete(@PathVariable UUID id) {
        _warehouseRuleService.deleteRule(id);
        return ApiResp.<String>builder().result("Deleted warehouse rule: " + id).build();
    }

    @GetMapping("/{id}")
    public ApiResp<WarehouseRuleResponseDto> getById(@PathVariable UUID id) {
        WarehouseRuleResponseDto result = _warehouseRuleService.getById(id);
        return ApiResp.<WarehouseRuleResponseDto>builder().result(result).build();
    }

    @GetMapping
    public ApiResp<List<WarehouseRuleResponseDto>> getAll() {
        List<WarehouseRuleResponseDto> result = _warehouseRuleService.getAll();
        return ApiResp.<List<WarehouseRuleResponseDto>>builder().result(result).build();
    }
}
