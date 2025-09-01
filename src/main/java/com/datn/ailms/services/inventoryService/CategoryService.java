package com.datn.ailms.services.inventoryService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.ICategoryService;
import com.datn.ailms.mapper.CategoryMapper;
import com.datn.ailms.model.dto.request.inventory.CategoryRequestDto;
import com.datn.ailms.model.dto.response.inventory.CategoryResponseDto;
import com.datn.ailms.model.entities.Category;
import com.datn.ailms.repositories.productRepo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {


    private final CategoryRepository _categoryRepository;
    private final CategoryMapper _categoryMapper;

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = _categoryRepository.findAll();

        return _categoryMapper.toResponseList(categories);
    }

    @Override
    public CategoryResponseDto getCategoryById(UUID categoryId) {
        Category category = _categoryRepository.findById(categoryId).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)
        );
        return _categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto request) {
        Category category = _categoryMapper.toEntity(request);
        category = _categoryRepository.save(category);
        return _categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponseDto updateCategory(UUID categoryId, CategoryRequestDto request) {
        Category category = _categoryRepository.findById(categoryId).orElseThrow(
                ()-> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)
        );
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updateCategory = _categoryRepository.save(category);

        return _categoryMapper.toResponse(updateCategory);
    }
}
