package com.datn.ailms.controllers.warehouse_controllers;

import com.datn.ailms.interfaces.IBinService;
import com.datn.ailms.model.dto.request.warehouse_request.CreateBinRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateBinRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.warehouse_response.BinResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bins")
@RequiredArgsConstructor
public class BinController {
    private final IBinService _binService;

    @GetMapping
    public ApiResp<List<BinResponseDto>> getAllBins() {
        return ApiResp.<List<BinResponseDto>>builder()
                .result(_binService.getAllBins())
                .build();
    }

    @GetMapping("/shelf/{shelfId}")
    public ApiResp<List<BinResponseDto>> getBinsByShelfId(@PathVariable UUID shelfId) {
        return ApiResp.<List<BinResponseDto>>builder()
                .result(_binService.findAllByShelfId(shelfId))
                .build();
    }

    @GetMapping("/{binId}")
    public ApiResp<BinResponseDto> getBinById(@PathVariable UUID binId) {
        return ApiResp.<BinResponseDto>builder()
                .result(_binService.getBinById(binId))
                .build();
    }

    @PostMapping
    public ApiResp<BinResponseDto> createBin(@RequestBody CreateBinRequestDto request) {
        return ApiResp.<BinResponseDto>builder()
                .result(_binService.createBin(request))
                .build();
    }

    @PutMapping("/{binId}")
    public ApiResp<BinResponseDto> updateBin(
            @PathVariable UUID binId,
            @RequestBody UpdateBinRequestDto request
    ) {
        return ApiResp.<BinResponseDto>builder()
                .result(_binService.updateBin(binId, request))
                .build();
    }
}