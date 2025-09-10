package com.datn.ailms.services.inventoryService;

import com.cloudinary.Cloudinary;
import com.datn.ailms.mapper.ProductMapper;
import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.model.entities.Category;
import com.datn.ailms.model.entities.Product;
import com.datn.ailms.model.entities.ProductImage;
import com.datn.ailms.repositories.productRepo.ProductImageRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository _productRepo;
    private final ProductImageRepository _productImageRepo;
    private final CloudinaryService _cloudinaryService;
    private final ProductMapper mapper;


    public ProductResponseDto create(ProductRequestDto request) {
        Product entity = mapper.toEntity(request);
        entity = _productRepo.save(entity);
        return mapper.toResponse(entity);
    }

    public ProductResponseDto createWithImages(ProductRequestDto request, MultipartFile[] images) throws IOException {
        Product product = mapper.toEntity(request);

         product = _productRepo.save(product);
        if (images != null) {
            for (MultipartFile image : images) {
                Map results = _cloudinaryService.uploadFile(image);
                ProductImage productImage = new ProductImage();
                productImage.setUrlImage(results.get("secure_url").toString());
                productImage.setPublicId(results.get("public_id").toString());
                productImage.setProduct(product);
                _productImageRepo.save(productImage);
            }
        }

        return mapper.toResponse(product);
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
