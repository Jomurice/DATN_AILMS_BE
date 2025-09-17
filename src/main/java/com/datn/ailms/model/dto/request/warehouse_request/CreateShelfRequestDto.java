package com.datn.ailms.model.dto.request.warehouse_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateShelfRequestDto {
    String code;
    String name;
    UUID warehouseId;
}
