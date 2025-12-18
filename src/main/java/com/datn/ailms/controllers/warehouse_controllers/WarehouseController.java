package com.datn.ailms.controllers.warehouse_controllers;

import com.datn.ailms.interfaces.IWarehouseService;
import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;


import com.datn.ailms.services.warehouseServices.WarehouseSerivce;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    public final WarehouseSerivce _warehouseService;

    @GetMapping
    public ApiResp<List<WarehouseResponseDto>> getAllWarehouses() {
        return ApiResp.<List<WarehouseResponseDto>>builder()
                .result(_warehouseService.findAll())
                .build();
    }

    @GetMapping("/tree/location/{location}")
    public ApiResp<List<WarehouseResponseDto>> getTreeByLocation(@PathVariable String location) {
        return ApiResp.<List<WarehouseResponseDto>>builder()
                .result(_warehouseService.findTreeByLocation(location))
                .build();
    }

    @GetMapping("/{warehouseId}")
    public ApiResp<WarehouseResponseDto> getWarehouseById(@PathVariable UUID warehouseId) {
        return ApiResp.<WarehouseResponseDto>builder()
                .result(_warehouseService.findById(warehouseId))
                .build();
    }


    @PostMapping
    public ApiResp<WarehouseResponseDto> createWarehouse(@RequestBody CreateWarehouseRequestDto request) {
        return ApiResp.<WarehouseResponseDto>builder()
                .result(_warehouseService.create(request))
                .build();
    }


    @PutMapping("/{warehouseId}")
    public ApiResp<WarehouseResponseDto> updateWarehouse(
            @PathVariable UUID warehouseId,
            @RequestBody UpdateWarehouseRequestDto request) {

        return ApiResp.<WarehouseResponseDto>builder()
                .result(_warehouseService.update(warehouseId, request))
                .build();
    }
}
