package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.inventory.CategoryRequestDto;
import com.datn.ailms.model.dto.response.inventory.CategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(UUID categoryId);
    CategoryResponseDto createCategory(CategoryRequestDto request);
    CategoryResponseDto updateCategory(UUID categoryId, CategoryRequestDto request );

}
