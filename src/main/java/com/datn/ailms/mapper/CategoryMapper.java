package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.inventory.CategoryRequestDto;
import com.datn.ailms.model.dto.response.inventory.CategoryResponseDto;
import com.datn.ailms.model.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto request);

    CategoryResponseDto toResponse(Category category);

    List<CategoryResponseDto> toResponseList(List<Category> categories);
}
