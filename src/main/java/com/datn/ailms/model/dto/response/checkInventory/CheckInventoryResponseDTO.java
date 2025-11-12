package com.datn.ailms.model.dto.response.checkInventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInventoryResponseDTO {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;  // "UNCHECKED" hoặc "CHECKED"
    private UUID createdBy;
    private UUID checkedBy;
    private UUID warehouseId;  // Thêm: ID kho
    private String warehouseName;  // Thêm: Tên kho (từ entity Warehouse)
    private Set<CheckInventoryItemResponseDTO> items;
}