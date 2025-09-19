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
public class ShelfResponseDto {
    UUID id;

    String code;
    String name;
    UUID aisleId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
