package com.datn.ailms.model.dto.request.warehouse_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateWarehouseRequestDto {
    String name;        // Tên warehouse
    String code;        // Mã warehouse, ví dụ HN01, ZONE01
    String type;        // ZONE, AISLE, SHELF, BIN
    UUID parentId;      // UUID warehouse cha, null nếu là cấp gốc (Zone)
    UUID locationId;    // UUID location mà warehouse thuộc về
}
