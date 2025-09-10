package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.brand_request.CreateBrandRequestDto;
import com.datn.ailms.model.dto.request.brand_request.UpdateBrandRequestDto;
import com.datn.ailms.model.dto.response.brand_response.BrandResponseDto;

import java.util.List;
import java.util.UUID;

public interface IBrand {
    BrandResponseDto createBrand(CreateBrandRequestDto request);
    BrandResponseDto updateBrand(UUID id, UpdateBrandRequestDto request);
    void deleteBrand(UUID id);
    BrandResponseDto getBrandById(UUID id);
    List<BrandResponseDto> getAllBrands();
}
