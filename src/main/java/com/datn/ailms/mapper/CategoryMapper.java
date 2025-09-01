package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.inventory.CategoryRequest;
import com.datn.ailms.model.dto.response.inventory.CategoryResponse;
import com.datn.ailms.model.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
