package com.datn.ailms.model.dto.response.inventory_check;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCheckResponseDto {
    UUID id;
    String code;
    UUID warehouseId;
    String warehouseName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String status;
    UUID createdBy;
    String createdByName;
    UUID checkedBy;
    String checkedByName;
    String note;
    LocalDateTime deadline;
}