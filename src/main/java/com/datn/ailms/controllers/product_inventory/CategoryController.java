package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.interfaces.ICategoryService;
import com.datn.ailms.model.dto.request.inventory.CategoryRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.CategoryDetailResponseDto;
import com.datn.ailms.model.dto.response.inventory.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final ICategoryService _categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LM')")
    public ApiResp<List<CategoryResponseDto>> getAllCategories(){
        return ApiResp.<List<CategoryResponseDto>>builder()
                .result(_categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResp<CategoryDetailResponseDto> getCategoryById(@PathVariable UUID categoryId){
        CategoryDetailResponseDto category = _categoryService.getCategoryById(categoryId);
        return ApiResp.<CategoryDetailResponseDto>builder()
                .result(category)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResp<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto request){
        CategoryResponseDto category = _categoryService.createCategory(request);
        return ApiResp.<CategoryResponseDto>builder()
                .result(category)
                .build();

    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResp<CategoryResponseDto> updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryRequestDto request){
        CategoryResponseDto category = _categoryService.updateCategory(categoryId, request);
        return  ApiResp.<CategoryResponseDto>builder()
                .result(category)
                .build();
    }
}
