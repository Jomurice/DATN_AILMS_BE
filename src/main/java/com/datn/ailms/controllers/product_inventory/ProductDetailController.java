package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.interfaces.IProductScanService;
import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.request.inventory.GenerateSerialForPORequest;
import com.datn.ailms.model.dto.request.inventory.ProductConfirmRequestDto;
import com.datn.ailms.model.dto.request.inventory.ProductGenerateSerialRequest;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.GenerateSerialForPOResponse;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.services.inventoryService.IProductConfirmService;
import com.datn.ailms.services.inventoryService.ProductConfirmService;
import com.datn.ailms.services.inventoryService.ProductScanPOService;
import com.datn.ailms.services.inventoryService.ProductScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailRepository _productDetailRepository;
    private final ProductDetailMapper _productDetailMapper;
    private final ProductScanService _productScanService;
    private final ProductConfirmService _productConfirmService;

    private final ProductScanPOService productScanPOService;

    @PostMapping("/create-serial-for-po")
    public ApiResp<GenerateSerialForPOResponse> createSerialForPO(
            @RequestBody GenerateSerialForPORequest request) {
        var result = productScanPOService.generateSerialsForPO(request);
        return ApiResp.<GenerateSerialForPOResponse>builder().result(result).build();
    }

    // 1️⃣ Lấy tất cả ProductDetail
    @GetMapping
    public ApiResp<List<ProductDetailResponseDto>> getAll() {
        var result = _productDetailRepository.findAll();
        var toList = _productDetailMapper.toResponseList(result);
        return ApiResp.<List<ProductDetailResponseDto>>builder().result(toList).build();
    }

    // 2️⃣ Lấy ProductDetail theo productId
    @GetMapping("/{productId}")
    public ApiResp<List<ProductDetailResponseDto>> getByProductId(@PathVariable UUID productId) {
        var result = _productDetailRepository.findByProductId(productId);
        var toList = _productDetailMapper.toResponseList(result);
        return ApiResp.<List<ProductDetailResponseDto>>builder().result(toList).build();
    }

    // 3️⃣ Tạo serial cho toàn bộ PO item (theo orderQuantity)
    @PostMapping("/create-serial")
    public ApiResp<List<ProductDetailResponseDto>> createSerial(@RequestBody ProductGenerateSerialRequest request) {
        List<ProductDetailResponseDto> result = _productScanService.generateSerials(request);
        return ApiResp.<List<ProductDetailResponseDto>>builder().result(result).build();
    }

    // 4️⃣ Tạo serial batch với số lượng tùy chọn
    @PostMapping("/create-serial-batch/{quantity}")
    public ApiResp<List<ProductDetailResponseDto>> createSerialBatch(
            @RequestBody ProductGenerateSerialRequest request,
            @PathVariable int quantity
    ) {
        List<ProductDetailResponseDto> result = _productScanService.generateSerialBatch(request, quantity);
        return ApiResp.<List<ProductDetailResponseDto>>builder().result(result).build();
    }

    // 5️⃣ Xác nhận scan nhập kho
    @PostMapping("/confirm-scan")
    public ApiResp<ProductDetailResponseDto> confirmScan(@RequestBody ProductConfirmRequestDto request) {
        ProductDetailResponseDto result = _productConfirmService.confirmSerial(request);
        return ApiResp.<ProductDetailResponseDto>builder().result(result).build();
    }

    // 6️⃣ Lấy ProductDetail theo serial
    @GetMapping("/by-serial")
    public ApiResp<ProductDetailResponseDto> getBySerial(@RequestParam String serial) {
        ProductDetail detail = _productDetailRepository.findBySerialNumber(serial)
                .orElseThrow(() -> new EntityNotFoundException("ProductDetail not found: " + serial));
        return ApiResp.<ProductDetailResponseDto>builder()
                .result(_productDetailMapper.toResponse(detail))
                .build();
    }
}
