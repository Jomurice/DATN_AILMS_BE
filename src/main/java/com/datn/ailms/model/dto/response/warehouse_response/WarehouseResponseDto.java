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
public class WarehouseResponseDto {
    UUID id;
    String name;
    String code;
    String type;
    UUID parentId;
    UUID locationId;
    List<WarehouseResponseDto> children; // tùy chọn, nếu muốn trả hierarchy
}
