package com.datn.ailms.controllers.warehouse_controllers;

import com.datn.ailms.interfaces.IShelfService;
import com.datn.ailms.model.dto.request.warehouse_request.CreateShelfRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateShelfRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.warehouse_response.ShelfResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shelves")
@RequiredArgsConstructor
public class ShelfController {
    private final IShelfService _shelfService;


    @GetMapping
    public ApiResp<List<ShelfResponseDto>> getAllShelves() {
        return ApiResp.<List<ShelfResponseDto>>builder()
                .result(_shelfService.getAllShelves())
                .build();
    }


    @GetMapping("/aisle/{aisleId}")
    public ApiResp<List<ShelfResponseDto>> getShelvesByAisleId(@PathVariable UUID aisleId) {
        return ApiResp.<List<ShelfResponseDto>>builder()
                .result(_shelfService.findAllByAisleIdNativeQuery(aisleId))
                .build();
    }


    @GetMapping("/{shelfId}")
    public ApiResp<ShelfResponseDto> getShelfById(@PathVariable UUID shelfId) {
        return ApiResp.<ShelfResponseDto>builder()
                .result(_shelfService.getShelfById(shelfId))
                .build();
    }


    @PostMapping
    public ApiResp<ShelfResponseDto> createShelf(@RequestBody CreateShelfRequestDto request) {
        return ApiResp.<ShelfResponseDto>builder()
                .result(_shelfService.createShelf(request))
                .build();
    }


    @PutMapping("/{shelfId}")
    public ApiResp<ShelfResponseDto> updateShelf(
            @PathVariable UUID shelfId,
            @RequestBody UpdateShelfRequestDto request
    ) {
        return ApiResp.<ShelfResponseDto>builder()
                .result(_shelfService.updateShelf(shelfId, request))
                .build();
    }



}