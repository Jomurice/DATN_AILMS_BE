package com.datn.ailms.model.dto.request.category_brand_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCategoryBrandRequestDto {
    UUID categoryId;
    UUID brandId;
}