package com.datn.ailms.interfaces.inventory_check;

import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckItemRequestDto;
import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckRequestDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckItemResponseDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IInventoryCheckService {

    // ✅ CẬP NHẬT: Trả về Page thay vì List, thêm tham số status và pageable
    Page<InventoryCheckResponseDto> getAll(String status, Pageable pageable);

    // (Giữ nguyên phương thức getAll cũ trả về List để tương thích ngược nếu cần, hoặc xóa đi nếu Interface đã đổi)
    List<InventoryCheckResponseDto> getAll();

    InventoryCheckResponseDto getById(UUID id);
    InventoryCheckResponseDto create(InventoryCheckRequestDto request);
    InventoryCheckResponseDto update(UUID id, InventoryCheckRequestDto request);
    void delete(UUID id);

    InventoryCheckResponseDto startCheck(UUID checkId, UUID checkedByUserId);
    InventoryCheckResponseDto completeCheck(UUID checkId);
    InventoryCheckResponseDto closeCheck(UUID checkId);

    List<String> suggestSerials(UUID checkId, String query);
    InventoryCheckItemResponseDto scanSerial(UUID checkId, String serialNumber, UUID scannedByUserId);
    List<InventoryCheckItemResponseDto> getItemsByCheckId(UUID checkId);
    InventoryCheckItemResponseDto addItemManual(UUID checkId, InventoryCheckItemRequestDto request);
    InventoryCheckItemResponseDto updateItemManual(UUID itemId, InventoryCheckItemRequestDto request);
    void deleteItemManual(UUID itemId);
}