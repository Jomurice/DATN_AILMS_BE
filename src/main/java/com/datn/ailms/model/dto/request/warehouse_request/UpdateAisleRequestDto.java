package com.datn.ailms.model.dto.request.warehouse_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAisleRequestDto {
    String code;
    String name;
}
