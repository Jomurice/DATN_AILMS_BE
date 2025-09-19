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

    private final IShelfService shelfService;

    @GetMapping
    public ApiResp<List<ShelfResponseDto>> getAllShelves() {
        return ApiResp.<List<ShelfResponseDto>>builder()
                .result(shelfService.getAllShelves())
                .build();
    }

    @GetMapping("/{shelfId}")
    public ApiResp<ShelfResponseDto> getShelfById(@PathVariable UUID shelfId) {
        return ApiResp.<ShelfResponseDto>builder()
                .result(shelfService.getShelfById(shelfId))
                .build();
    }

    @PostMapping
    public ApiResp<ShelfResponseDto> createShelf(@RequestBody CreateShelfRequestDto request) {
        return ApiResp.<ShelfResponseDto>builder()
                .result(shelfService.createShelf(request))
                .build();
    }

    @PutMapping("/{shelfId}")
    public ApiResp<ShelfResponseDto> updateShelf(
            @PathVariable UUID shelfId,
            @RequestBody UpdateShelfRequestDto request
    ) {
        return ApiResp.<ShelfResponseDto>builder()
                .result(shelfService.updateShelf(shelfId, request))
                .build();
    }
}