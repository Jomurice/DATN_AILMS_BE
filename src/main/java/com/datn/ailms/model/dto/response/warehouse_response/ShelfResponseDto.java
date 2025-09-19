package com.datn.ailms.model.dto.response.warehouse_response;

import java.time.LocalDateTime;
import java.util.UUID;

public class ShelfResponseDto {
    UUID id;

    String code;
    String name;
    UUID warehouseId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
