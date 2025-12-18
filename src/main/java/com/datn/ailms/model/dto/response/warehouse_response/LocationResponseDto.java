package com.datn.ailms.model.dto.response.warehouse_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponseDto {
    private UUID id;
    private String name;
    String address;
    private UUID parentId; // null nếu root
    private List<LocationResponseDto> children; // null hoặc list con
}
