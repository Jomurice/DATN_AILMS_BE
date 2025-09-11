package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.model.entities.product_entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // RequestDto -> Entity (service sẽ set categoryBrand)
    @Mapping(target = "categoryBrand", ignore = true)
    @Mapping(target = "categoryId", source = "categoryBrand.category.id")
    @Mapping(target = "categoryName", source = "categoryBrand.category.name")
    @Mapping(target = "brandId", source = "categoryBrand.brand.id")
    @Mapping(target = "brandName", source = "categoryBrand.brand.name")
    @Mapping(target = "menuId", source = "categoryBrand.category.menu.id")
    @Mapping(target = "menuTitle", source = "categoryBrand.category.menu.title")
    Product toEntity(ProductRequestDto request);

    // Entity -> ResponseDto
    @Mapping(target = "categoryId", source = "categoryBrand.category.id")
    @Mapping(target = "categoryName", source = "categoryBrand.category.name")
    @Mapping(target = "brandId", source = "categoryBrand.brand.id")
    @Mapping(target = "brandName", source = "categoryBrand.brand.name")
    @Mapping(target = "menuId", source = "categoryBrand.category.menu.id")
    @Mapping(target = "menuTitle", source = "categoryBrand.category.menu.title")
    ProductResponseDto toResponse(Product product);

    List<ProductResponseDto> toResponseList(List<Product> products);
}
