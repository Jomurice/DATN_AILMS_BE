package com.datn.ailms.model.dto.request.warehouse_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateWarehouseRequestDto {
    String name;        // Tên warehouse
    String code;        // Mã warehouse, ví dụ HN01, ZONE01
    String type;        // ZONE, AISLE, SHELF, BIN
    Integer currentQuantity;;
    Integer capacity;
    UUID parentId;      // UUID warehouse cha, null nếu là cấp gốc (Zone)
    String location;    // UUID location mà warehouse thuộc về

}
