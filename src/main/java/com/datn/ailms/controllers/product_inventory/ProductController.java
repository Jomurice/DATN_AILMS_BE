package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.services.inventoryService.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService _productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
     ApiResp<ProductResponseDto> create(@RequestBody ProductRequestDto request) {
              var result =  _productService.create(request);
              return ApiResp.<ProductResponseDto>builder().result(result).build();
    }

    @GetMapping("/api/products/by-menu/{menuId}")
    public List<ProductResponseDto> getProductsByMenu(@PathVariable UUID menuId) {
        return _productService.getByMenuId(menuId);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
     ApiResp<List<ProductResponseDto>> getAll() {
        var result =  _productService.getAll();
        return ApiResp.<List<ProductResponseDto>>builder().result(result).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    ApiResp<ProductResponseDto> getById(@PathVariable UUID id) {
        var result = _productService.getById(id);
        return ApiResp.<ProductResponseDto>builder().result(result).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    ApiResp<ProductResponseDto> update(
            @PathVariable UUID id,
            @RequestBody ProductRequestDto request) {
        var result =  _productService.update(id, request);
        return ApiResp.<ProductResponseDto>builder()
                .result(result)
                .build();
    }


    @GetMapping("/search-products")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResp<Page<ProductResponseDto>> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID brandId
    ) {
        Page<ProductResponseDto> products =
                _productService.searchProducts(page, size, name, categoryId, brandId);

        return ApiResp.<Page<ProductResponseDto>>builder()
                .message("Get products successfully")
                .result(products)
                .build();
    }

}

