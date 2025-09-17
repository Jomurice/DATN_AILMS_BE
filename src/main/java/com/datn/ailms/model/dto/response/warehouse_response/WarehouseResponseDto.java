package com.datn.ailms.model.dto.response.warehouse_response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseResponseDto {
    UUID id;

    String code;

    String name;

    String location;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
