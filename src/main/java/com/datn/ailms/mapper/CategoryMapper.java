package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.inventory.CategoryRequestDto;
import com.datn.ailms.model.dto.response.inventory.CategoryDetailResponseDto;
import com.datn.ailms.model.dto.response.inventory.CategoryResponseDto;
import com.datn.ailms.model.entities.product_entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto request);

    @Mapping(source = "menu.id", target = "menu.id")
    @Mapping(source = "menu.title", target = "menu.title")
    @Mapping(source = "menu.path", target = "menu.path")
    @Mapping(source = "menu.parent.id", target = "menu.parentId")
    CategoryResponseDto toResponse(Category category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    CategoryDetailResponseDto toDetailResponse(Category category);


    List<CategoryResponseDto> toResponseList(List<Category> categories);
}
