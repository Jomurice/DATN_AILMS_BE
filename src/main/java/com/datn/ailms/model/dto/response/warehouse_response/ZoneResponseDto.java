package com.datn.ailms.model.dto.response.warehouse_response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZoneResponseDto {
    UUID id;
    String name;
    String code;
    UUID warehouseId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    List<AisleResponseDto> aisles;
}
