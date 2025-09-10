package com.datn.ailms.services;

import com.datn.ailms.interfaces.IBrand;
import com.datn.ailms.model.dto.request.brand_request.CreateBrandRequestDto;
import com.datn.ailms.model.dto.request.brand_request.UpdateBrandRequestDto;
import com.datn.ailms.model.dto.response.brand_response.BrandResponseDto;
import com.datn.ailms.model.entities.Brand;
import com.datn.ailms.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrand {

    private final BrandRepository brandRepo;

    @Override
    public BrandResponseDto createBrand(CreateBrandRequestDto request) {
        if (brandRepo.existsByName(request.getName())) {
            throw new RuntimeException("Brand already exists: " + request.getName());
        }

        Brand brand = Brand.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .build();

        brand = brandRepo.save(brand);

        return mapToDto(brand);
    }

    @Override
    public BrandResponseDto updateBrand(UUID id, UpdateBrandRequestDto request) {
        Brand brand = brandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found: " + id));

        brand.setName(request.getName());

        brand = brandRepo.save(brand);

        return mapToDto(brand);
    }

    @Override
    public void deleteBrand(UUID id) {
        Brand brand = brandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found: " + id));
        brandRepo.delete(brand);
    }

    @Override
    public BrandResponseDto getBrandById(UUID id) {
        Brand brand = brandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found: " + id));
        return mapToDto(brand);
    }

    @Override
    public List<BrandResponseDto> getAllBrands() {
        return brandRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BrandResponseDto mapToDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
