package com.datn.ailms.model.dto.request.warehouse_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateWarehouseRequestDto {
    String code;
    String name;
    String location;

}
