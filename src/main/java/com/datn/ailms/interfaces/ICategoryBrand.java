package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.category_brand_request.CreateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.request.category_brand_request.UpdateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.response.category_brand_response.CategoryBrandResponseDto;

import java.util.List;
import java.util.UUID;

public interface ICategoryBrand {
    CategoryBrandResponseDto createCategoryBrand(CreateCategoryBrandRequestDto request);
    CategoryBrandResponseDto updateCategoryBrand(UUID id, UpdateCategoryBrandRequestDto request);
    void deleteCategoryBrand(UUID id);
    CategoryBrandResponseDto getCategoryBrandById(UUID id);
    List<CategoryBrandResponseDto> getAllCategoryBrands();
}
