package com.datn.ailms.mapper;


import com.datn.ailms.model.dto.request.category_brand_request.CreateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.request.category_brand_request.UpdateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.response.category_brand_response.CategoryBrandResponseDto;
import com.datn.ailms.model.entities.CategoryBrand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryBrandMapper {

    @Mapping(target = "category", ignore = true) // set trong service
    @Mapping(target = "brand", ignore = true)
    CategoryBrand toEntity(CreateCategoryBrandRequestDto request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    CategoryBrand toEntity(UpdateCategoryBrandRequestDto request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "brand.name", target = "brandName")
    CategoryBrandResponseDto toResponse(CategoryBrand categoryBrand);
}