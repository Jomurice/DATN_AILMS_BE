package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.services.inventoryService.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService _productService;

    @PostMapping
     ApiResp<ProductResponseDto> create(@RequestBody ProductRequestDto request) {
              var result =  _productService.create(request);
              return ApiResp.<ProductResponseDto>builder().result(result).build();
    }

    @GetMapping
     ApiResp<List<ProductResponseDto>> getAll() {
        var result =  _productService.getAll();
        return ApiResp.<List<ProductResponseDto>>builder().result(result).build();
    }

    @GetMapping("/{id}")
    ApiResp<ProductResponseDto> getById(@PathVariable UUID id) {
        var result = _productService.getById(id);
        return ApiResp.<ProductResponseDto>builder().result(result).build();
    }

    @PutMapping("/{id}")
    ApiResp<ProductResponseDto> update(
            @PathVariable UUID id,
            @RequestBody ProductRequestDto request) {
        var result =  _productService.update(id, request);
        return ApiResp.<ProductResponseDto>builder()
                .result(result)
                .build();
    }

}
