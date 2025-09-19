package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.IAisleService;
import com.datn.ailms.model.dto.request.warehouse_request.CreateAisleRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateAisleRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.warehouse_response.AisleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aisles")
@RequiredArgsConstructor
public class AisleController {
    private final IAisleService _aisleService;


    @GetMapping
    public ApiResp<List<AisleResponseDto>> getAllAisles() {
        return ApiResp.<List<AisleResponseDto>>builder()
                .result(_aisleService.getAllAisles())
                .build();
    }


    @GetMapping("/zone/{zoneId}")
    public ApiResp<List<AisleResponseDto>> getAislesByZoneId(@PathVariable UUID zoneId) {
        return ApiResp.<List<AisleResponseDto>>builder()
                .result(_aisleService.findAllByZoneIdNativeQuery(zoneId))
                .build();
    }


    @GetMapping("/{aisleId}")
    public ApiResp<AisleResponseDto> getAisleById(@PathVariable UUID aisleId) {
        return ApiResp.<AisleResponseDto>builder()
                .result(_aisleService.getAisleById(aisleId))
                .build();
    }


    @PostMapping
    public ApiResp<AisleResponseDto> createAisle(@RequestBody CreateAisleRequestDto request) {
        return ApiResp.<AisleResponseDto>builder()
                .result(_aisleService.createAisle(request))
                .build();
    }


    @PutMapping("/{aisleId}")
    public ApiResp<AisleResponseDto> updateAisle(
            @PathVariable UUID aisleId,
            @RequestBody UpdateAisleRequestDto request
    ) {
        return ApiResp.<AisleResponseDto>builder()
                .result(_aisleService.updateAisle(aisleId, request))
                .build();
    }


}