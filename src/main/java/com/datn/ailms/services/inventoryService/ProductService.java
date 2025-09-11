package com.datn.ailms.services.inventoryService;

import com.datn.ailms.mapper.ProductMapper;
import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.model.entities.CategoryBrand;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.repositories.categoryBrand.CategoryBrandRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryBrandRepository categoryBrandRepo;
    private final ProductMapper mapper;

    public ProductResponseDto create(ProductRequestDto request) {
        Product entity = mapper.toEntity(request);

        // tìm CategoryBrand theo categoryId + brandId
        CategoryBrand categoryBrand = categoryBrandRepo
                .findByCategoryIdAndBrandId(request.getCategoryId(), request.getBrandId())
                .orElseThrow(() -> new RuntimeException("CategoryBrand not found"));

        entity.setCategoryBrand(categoryBrand);

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

        // gán CategoryBrand mới nếu thay đổi
        CategoryBrand categoryBrand = categoryBrandRepo
                .findByCategoryIdAndBrandId(request.getCategoryId(), request.getBrandId())
                .orElseThrow(() -> new RuntimeException("CategoryBrand not found"));

        updated.setCategoryBrand(categoryBrand);

        updated = productRepo.save(updated);
        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {
        productRepo.deleteById(id);
    }
}
