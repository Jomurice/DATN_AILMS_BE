package com.datn.ailms.model.dto.request.inventory_check;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCheckRequestDto {
    UUID warehouseId;
    UUID createdBy;
    UUID checkedBy;
    String note;
    LocalDateTime deadline;
}