package com.datn.ailms.services.categoryBrandServices;

import com.datn.ailms.interfaces.ICategoryBrand;
import com.datn.ailms.mapper.CategoryBrandMapper;
import com.datn.ailms.model.dto.request.category_brand_request.CreateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.request.category_brand_request.UpdateCategoryBrandRequestDto;
import com.datn.ailms.model.dto.response.category_brand_response.CategoryBrandResponseDto;
import com.datn.ailms.model.entities.Brand;
import com.datn.ailms.model.entities.CategoryBrand;
import com.datn.ailms.model.entities.product_entities.Category;
import com.datn.ailms.repositories.BrandRepository;
import com.datn.ailms.repositories.categoryBrand.CategoryBrandRepository;
import com.datn.ailms.repositories.productRepo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryBrandService implements ICategoryBrand {

    private final CategoryBrandRepository categoryBrandRepo;
    private final CategoryRepository categoryRepo;
    private final BrandRepository brandRepo;
    private final CategoryBrandMapper mapper;

    @Override
    public CategoryBrandResponseDto createCategoryBrand(CreateCategoryBrandRequestDto request) {
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));
        Brand brand = brandRepo.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found: " + request.getBrandId()));

        // Kiểm tra tồn tại
        // Tìm theo categoryId + brandId
        CategoryBrand cb = categoryBrandRepo.findByCategoryIdAndBrandId(category.getId(), brand.getId())
                .orElse(null);

        if (cb == null) {
            // Nếu chưa có thì tạo mới
            cb = new CategoryBrand();
            cb.setId(UUID.randomUUID());
            cb.setCategory(category);
            cb.setBrand(brand);
            cb = categoryBrandRepo.save(cb);
        }

        return mapper.toResponse(cb);
    }

    @Override
    public CategoryBrandResponseDto updateCategoryBrand(UUID id, UpdateCategoryBrandRequestDto request) {
        CategoryBrand cb = categoryBrandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("CategoryBrand not found: " + id));

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));
        Brand brand = brandRepo.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found: " + request.getBrandId()));

        cb.setCategory(category);
        cb.setBrand(brand);

        cb = categoryBrandRepo.save(cb);
        return mapper.toResponse(cb);
    }

    @Override
    public void deleteCategoryBrand(UUID id) {
        CategoryBrand cb = categoryBrandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("CategoryBrand not found: " + id));
        categoryBrandRepo.delete(cb);
    }

    @Override
    public CategoryBrandResponseDto getCategoryBrandById(UUID id) {
        CategoryBrand cb = categoryBrandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("CategoryBrand not found: " + id));
        return mapper.toResponse(cb);
    }

    @Override
    public List<CategoryBrandResponseDto> getAllCategoryBrands() {
        return categoryBrandRepo.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
