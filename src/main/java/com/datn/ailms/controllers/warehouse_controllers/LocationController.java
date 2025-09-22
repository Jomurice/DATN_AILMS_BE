package com.datn.ailms.controllers.warehouse_controllers;

import com.datn.ailms.interfaces.ILocationService;
import com.datn.ailms.model.dto.request.warehouse_request.CreateLocationRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.warehouse_response.LocationResponseDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final ILocationService locationService;

    // Lấy tất cả locations
    @GetMapping
    public ApiResp<List<LocationResponseDto>> getAllLocations() {
        return ApiResp.<List<LocationResponseDto>>builder()
                .result(locationService.findTree())
                .build();
    }

    // Lấy location theo ID
    @GetMapping("/{locationId}")
    public ApiResp<LocationResponseDto> getLocationById(@PathVariable UUID locationId) {
        return ApiResp.<LocationResponseDto>builder()
                .result(locationService.findById(locationId))
                .build();
    }

    // Lấy location theo warehouseId
    @GetMapping("/by-warehouse/{warehouseId}")
    public ApiResp<LocationResponseDto> getLocationByWarehouse(@PathVariable UUID warehouseId) {
        return ApiResp.<LocationResponseDto>builder()
                .result(locationService.findLocationByWarehouseId(warehouseId))
                .build();
    }

    // Lấy warehouses theo locationId
    @GetMapping("/{locationId}/warehouses")
    public ApiResp<List<WarehouseResponseDto>> getWarehousesByLocation(@PathVariable UUID locationId) {
        return ApiResp.<List<WarehouseResponseDto>>builder()
                .result(locationService.findWarehousesByLocationId(locationId))
                .build();
    }

    // Tạo location mới
    @PostMapping
    public ApiResp<LocationResponseDto> createLocation(@RequestBody CreateLocationRequestDto request) {
        return ApiResp.<LocationResponseDto>builder()
                .result(locationService.create(request))
                .build();
    }

    // Cập nhật location
    @PutMapping("/{locationId}")
    public ApiResp<LocationResponseDto> updateLocation(
            @PathVariable UUID locationId,
            @RequestBody CreateLocationRequestDto request) {
        return ApiResp.<LocationResponseDto>builder()
                .result(locationService.update(locationId, request))
                .build();
    }

    // Xoá location
    @DeleteMapping("/{locationId}")
    public ApiResp<Boolean> deleteLocation(@PathVariable UUID locationId) {
        locationService.delete(locationId);
        return ApiResp.<Boolean>builder()
                .result(true)
                .build();
    }
}
