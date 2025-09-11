package com.datn.ailms.services.inventoryService;

import com.datn.ailms.mapper.ProductMapper;
import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.model.entities.Category;
import com.datn.ailms.model.entities.Product;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository _productRepo;
    private final ProductMapper mapper;

    public ProductResponseDto create(ProductRequestDto request) {
        Product entity = mapper.toEntity(request);
        entity = _productRepo.save(entity);
        return mapper.toResponse(entity);
    }

    public List<ProductResponseDto> getAll() {
        return mapper.toResponseList(_productRepo.findAll());
    }

    public ProductResponseDto getById(UUID id) {
        return mapper.toResponse(_productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found")));
    }

    public ProductResponseDto update(UUID id, ProductRequestDto request) {
        Product existing = _productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        // update các field cần thiết
        existing.setName(request.getName());
        existing.setSku(request.getSku());
        existing.setBrand(request.getBrand());
        existing.setSpecifications(request.getSpecifications());
        existing.setColor(request.getColor());
        existing.setStorage(request.getStorage());
        if (request.getCategoryId() != null) {
            Category category = new Category();
            category.setId(request.getCategoryId());
            existing.setCategory(category);
        }
        existing = _productRepo.save(existing);
        return mapper.toResponse(existing);
    }

    public void delete(UUID id) {
        _productRepo.deleteById(id);
    }
}
