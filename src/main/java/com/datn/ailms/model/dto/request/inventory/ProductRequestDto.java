package com.datn.ailms.model.dto.request.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    private String sku;
    private String name;
    private String brand;
    private String specifications;
    private String color;
    private String storage;
    private String serialPrefix;
    private UUID categoryId;
    private UUID brandId;
}
