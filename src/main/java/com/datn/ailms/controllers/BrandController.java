package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.IBrand;
import com.datn.ailms.model.dto.request.brand_request.CreateBrandRequestDto;
import com.datn.ailms.model.dto.request.brand_request.UpdateBrandRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.brand_response.BrandResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final IBrand brandService;

    @PostMapping
    public ApiResp<BrandResponseDto> create(@RequestBody CreateBrandRequestDto request) {
        BrandResponseDto result = brandService.createBrand(request);
        return ApiResp.<BrandResponseDto>builder().result(result).build();
    }

    @PutMapping("/{id}")
    public ApiResp<BrandResponseDto> update(@PathVariable UUID id, @RequestBody UpdateBrandRequestDto request) {
        BrandResponseDto result = brandService.updateBrand(id, request);
        return ApiResp.<BrandResponseDto>builder().result(result).build();
    }

    @DeleteMapping("/{id}")
    public ApiResp<String> delete(@PathVariable UUID id) {
        brandService.deleteBrand(id);
        return ApiResp.<String>builder().result("Deleted brand: " + id).build();
    }

    @GetMapping("/{id}")
    public ApiResp<BrandResponseDto> getById(@PathVariable UUID id) {
        BrandResponseDto result = brandService.getBrandById(id);
        return ApiResp.<BrandResponseDto>builder().result(result).build();
    }

    @GetMapping
    public ApiResp<List<BrandResponseDto>> getAll() {
        List<BrandResponseDto> result = brandService.getAllBrands();
        return ApiResp.<List<BrandResponseDto>>builder().result(result).build();
    }
}
