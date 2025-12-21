package com.datn.ailms.controllers.inventory_controller;

import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckRequestDto;
import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckItemRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckItemResponseDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckResponseDto;
import com.datn.ailms.interfaces.inventory_check.IInventoryCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
public class InventoryCheckController {

    final IInventoryCheckService _checkService;

    // ✅ GET ALL (PHÂN TRANG & LỌC) - CHỈ GIỮ HÀM NÀY
    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN','LM')")
    public ApiResp<Page<InventoryCheckResponseDto>> getAll(
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResp.<Page<InventoryCheckResponseDto>>builder()
                .result(_checkService.getAll(status, pageable))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResp<InventoryCheckResponseDto> getById(@PathVariable UUID id) {
        return ApiResp.<InventoryCheckResponseDto>builder().result(_checkService.getById(id)).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LM')")
    public ApiResp<InventoryCheckResponseDto> create(@RequestBody InventoryCheckRequestDto request) {
        return ApiResp.<InventoryCheckResponseDto>builder().result(_checkService.create(request)).build();
    }

    @PutMapping("/{id}")
    public ApiResp<InventoryCheckResponseDto> update(@PathVariable UUID id, @RequestBody InventoryCheckRequestDto request) {
        return ApiResp.<InventoryCheckResponseDto>builder().result(_checkService.update(id, request)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResp<Void> delete(@PathVariable UUID id) {
        _checkService.delete(id);
        return ApiResp.<Void>builder().message("Deleted Inventory Check: " + id).build();
    }
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ApiResp<Void> delete(@PathVariable UUID id) {
//        _checkService.delete(id);
//        return ApiResp.<Void>builder().message("Deleted Inventory Check: " + id).build();
//    }
    @PostMapping("/{id}/start")
    public ApiResp<InventoryCheckResponseDto> startCheck(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID checkedByUserId
    ) {
        return ApiResp.<InventoryCheckResponseDto>builder().result(_checkService.startCheck(id, checkedByUserId)).build();
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','LM')")
    public ApiResp<InventoryCheckResponseDto> completeCheck(@PathVariable UUID id) {
        return ApiResp.<InventoryCheckResponseDto>builder().result(_checkService.completeCheck(id)).build();
    }

    @GetMapping("/{id}/summary")
    public ApiResp<String> getSummary(@PathVariable UUID id) {
        return ApiResp.<String>builder().result("Summary for Check ID: " + id).build();
    }
    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN','LM')")
    public ApiResp<InventoryCheckResponseDto> closeCheck(@PathVariable UUID id) {
        return ApiResp.<InventoryCheckResponseDto>builder()
                .result(_checkService.closeCheck(id))
                .build();
    }
//    @PostMapping("/{id}/close")
//    // ✅ Sửa thành hasAuthority('ADMIN') để khớp chính xác với DB
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ApiResp<InventoryCheckResponseDto> closeCheck(@PathVariable UUID id) {
//        return ApiResp.<InventoryCheckResponseDto>builder()
//                .result(_checkService.closeCheck(id))
//                .build();
//    }

    // --- Quét Serial ---

    @PostMapping("/{id}/scan")
    public ApiResp<InventoryCheckItemResponseDto> scanSerial(
            @PathVariable UUID id,
            @RequestParam String serialNumber,
            @RequestParam(required = false) UUID scannedByUserId
    ) {
        return ApiResp.<InventoryCheckItemResponseDto>builder()
                .result(_checkService.scanSerial(id, serialNumber, scannedByUserId))
                .build();
    }
    @GetMapping("/{id}/serials/suggest")
    public ApiResp<List<String>> suggestSerials(
            @PathVariable UUID id,
            @RequestParam(name = "q") String query
    ) {
        return ApiResp.<List<String>>builder()
                .result(_checkService.suggestSerials(id, query))
                .build();
    }
    // --- Chi tiết Item (CRUD thủ công) ---

    @GetMapping("/{id}/items")
    public ApiResp<List<InventoryCheckItemResponseDto>> getItemsByCheckId(@PathVariable UUID id) {
        return ApiResp.<List<InventoryCheckItemResponseDto>>builder().result(_checkService.getItemsByCheckId(id)).build();
    }

    @PostMapping("/{id}/items")
    public ApiResp<InventoryCheckItemResponseDto> addItemManual(
            @PathVariable UUID id,
            @RequestBody InventoryCheckItemRequestDto request
    ) {
        return ApiResp.<InventoryCheckItemResponseDto>builder().result(_checkService.addItemManual(id, request)).build();
    }

    @PutMapping("/{id}/items/{itemId}")
    public ApiResp<InventoryCheckItemResponseDto> updateItem(
            @PathVariable UUID itemId,
            @RequestBody InventoryCheckItemRequestDto request
    ) {
        return ApiResp.<InventoryCheckItemResponseDto>builder().result(_checkService.updateItemManual(itemId, request)).build();
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ApiResp<Void> deleteItem(@PathVariable UUID itemId) {
        _checkService.deleteItemManual(itemId);
        return ApiResp.<Void>builder().message("Deleted Inventory Check Item: " + itemId).build();
    }
}