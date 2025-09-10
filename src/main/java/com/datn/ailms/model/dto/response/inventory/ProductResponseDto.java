package com.datn.ailms.model.dto.response.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private UUID id;
    private String sku;
    private String name;
    private String specifications;
    private String color;
    private String storage;
    private String serialPrefix;

    private UUID categoryId;
    private String categoryName;

    private UUID brandId;
    private String brandName;

    private UUID menuId;
    private String menuTitle;
}
