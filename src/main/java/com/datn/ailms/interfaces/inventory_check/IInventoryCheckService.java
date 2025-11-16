package com.datn.ailms.interfaces.inventory_check;

import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckItemRequestDto;
import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckRequestDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckItemResponseDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckResponseDto;

import java.util.List;
import java.util.UUID;

public interface IInventoryCheckService {
    // Phiếu kiểm kê (CRUD + Flow)
    List<InventoryCheckResponseDto> getAll();
    InventoryCheckResponseDto getById(UUID id);
    InventoryCheckResponseDto create(InventoryCheckRequestDto request);
    InventoryCheckResponseDto update(UUID id, InventoryCheckRequestDto request);
    void delete(UUID id);

    InventoryCheckResponseDto startCheck(UUID checkId, UUID checkedByUserId);
    InventoryCheckResponseDto completeCheck(UUID checkId);

    // ✅ BỔ SUNG: Chốt sổ phiếu kiểm kê (CLOSE)
    InventoryCheckResponseDto closeCheck(UUID checkId);

    //API MỚI: Gợi ý Serial trong phiếu (cho Autocomplete)
    List<String> suggestSerials(UUID checkId, String query);
    // Quét Serial
    InventoryCheckItemResponseDto scanSerial(UUID checkId, String serialNumber, UUID scannedByUserId);

    // Chi tiết Item (CRUD thủ công)
    List<InventoryCheckItemResponseDto> getItemsByCheckId(UUID checkId);
    InventoryCheckItemResponseDto addItemManual(UUID checkId, InventoryCheckItemRequestDto request);
    InventoryCheckItemResponseDto updateItemManual(UUID itemId, InventoryCheckItemRequestDto request);
    void deleteItemManual(UUID itemId);
}