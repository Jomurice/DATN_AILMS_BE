package com.datn.ailms.model.dto.response.category_brand_response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryBrandResponseDto {
    UUID id;
    UUID categoryId;
    String categoryName;
    UUID brandId;
    String brandName;
}