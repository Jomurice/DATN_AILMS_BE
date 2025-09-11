package com.datn.ailms.model.dto.response.brand_response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponseDto {
    UUID id;
    String name;
}
