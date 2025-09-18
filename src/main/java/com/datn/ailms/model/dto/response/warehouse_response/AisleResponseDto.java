package com.datn.ailms.model.dto.response.warehouse_response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AisleResponseDto {
    UUID id;
    String code;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UUID zoneId;
    List<ShelfResponseDto> shelfList;
}
