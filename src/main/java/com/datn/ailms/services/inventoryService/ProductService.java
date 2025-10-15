package com.datn.ailms.services.inventoryService;

import com.datn.ailms.mapper.ProductMapper;
import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.model.entities.Brand;
import com.datn.ailms.model.entities.product_entities.Category;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.repositories.BrandRepository;
import com.datn.ailms.repositories.productRepo.CategoryRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepo;
    private final ProductMapper mapper;
    private final BrandRepository _brandRepo;
    private final CategoryRepository _categoryRepo;


    public ProductResponseDto create(ProductRequestDto request) {
        Product entity = mapper.toEntity(request);

        // Lấy category & brand
        Category category = _categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Brand brand = _brandRepo.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        entity.setBrands(Collections.singleton(brand));
        entity.setCategory(category);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        entity = productRepo.save(entity);
        return mapper.toResponse(entity);
    }

    public List<ProductResponseDto> getByMenuId(UUID menuId) {
        return productRepo.findByMenuId(menuId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getAll() {
        return mapper.toResponseList(productRepo.findAll());
    }

    public ProductResponseDto getById(UUID id) {
        return mapper.toResponse(productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found")));
    }

    public ProductResponseDto update(UUID id, ProductRequestDto request) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // dùng mapper để map lại fields cơ bản
        Product updated = mapper.toEntity(request);

        // giữ lại id cũ
        updated.setId(existing.getId());
        updated.setUpdatedAt(LocalDateTime.now());
        Category category = _categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updated.setCategory(category);

        // Brand
        Brand brand = _brandRepo.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        updated.setBrands(Collections.singleton(brand));


        updated = productRepo.save(updated);
        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {
        productRepo.deleteById(id);
    }


}
