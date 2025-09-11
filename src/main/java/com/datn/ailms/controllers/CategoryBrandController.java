package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.ICategoryBrand;
import com.datn.ailms.model.dto.request.category_brand_request.CreateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.request.category_brand_request.UpdateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.category_brand_response.CategoryBrandResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category-brands")
@RequiredArgsConstructor
public class CategoryBrandController {

    private final ICategoryBrand categoryBrandService;

    @PostMapping
    public ApiResp<CategoryBrandResponseDto> create(@RequestBody CreateCategoryBrandRequestDto request) {
        CategoryBrandResponseDto result = categoryBrandService.createCategoryBrand(request);
        return ApiResp.<CategoryBrandResponseDto>builder().result(result).build();
    }

    @PutMapping("/{id}")
    public ApiResp<CategoryBrandResponseDto> update(@PathVariable UUID id, @RequestBody UpdateCategoryBrandRequestDto request) {
        CategoryBrandResponseDto result = categoryBrandService.updateCategoryBrand(id, request);
        return ApiResp.<CategoryBrandResponseDto>builder().result(result).build();
    }

    @DeleteMapping("/{id}")
    public ApiResp<String> delete(@PathVariable UUID id) {
        categoryBrandService.deleteCategoryBrand(id);
        return ApiResp.<String>builder().result("Deleted CategoryBrand: " + id).build();
    }

    @GetMapping("/{id}")
    public ApiResp<CategoryBrandResponseDto> getById(@PathVariable UUID id) {
        CategoryBrandResponseDto result = categoryBrandService.getCategoryBrandById(id);
        return ApiResp.<CategoryBrandResponseDto>builder().result(result).build();
    }

    @GetMapping
    public ApiResp<List<CategoryBrandResponseDto>> getAll() {
        List<CategoryBrandResponseDto> result = categoryBrandService.getAllCategoryBrands();
        return ApiResp.<List<CategoryBrandResponseDto>>builder().result(result).build();
    }
}
