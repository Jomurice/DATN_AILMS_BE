package com.datn.ailms.controllers.warehouse_controllers;

import com.datn.ailms.interfaces.IZoneService;
import com.datn.ailms.model.dto.request.warehouse_request.CreateZoneRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateZoneRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.warehouse_response.ZoneResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final IZoneService _zoneService;


    @GetMapping
    public ApiResp<List<ZoneResponseDto>> getAllZones() {
        return ApiResp.<List<ZoneResponseDto>>builder()
                .result(_zoneService.getAllZones())
                .build();
    }


    @GetMapping("/{zoneId}")
    public ApiResp<ZoneResponseDto> getZoneById(@PathVariable UUID zoneId) {
        return ApiResp.<ZoneResponseDto>builder()
                .result(_zoneService.getZoneById(zoneId))
                .build();
    }

    @PostMapping()
    public ApiResp<ZoneResponseDto> createZone(@RequestBody CreateZoneRequestDto request) {
        return ApiResp.<ZoneResponseDto>builder()
                .result(_zoneService.createZone(request))
                .build();
    }


    @PutMapping("/{zoneId}")
    public ApiResp<ZoneResponseDto> updateZone(
            @PathVariable UUID zoneId,
            @RequestBody UpdateZoneRequestDto request
    ) {
        return ApiResp.<ZoneResponseDto>builder()
                .result(_zoneService.updateZone(zoneId, request))
                .build();
    }
}
