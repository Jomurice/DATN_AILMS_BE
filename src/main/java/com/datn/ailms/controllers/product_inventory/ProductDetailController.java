package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailRepository _productDetailRepository;
    private final ProductDetailMapper _productDetailMapper;
    private final ProductRepository productRepo;

    @GetMapping
    ApiResp<List<ProductDetailResponseDto>> getAll(){
        var result = _productDetailRepository.findAll();
        var toList = _productDetailMapper.toResponseList(result);
        return ApiResp.<List<ProductDetailResponseDto>>builder().result(toList).build();
    }

    // API tạo 1 serial mới chưa gán bin
    @PostMapping("/create-serial")
    public ApiResp<ProductDetailResponseDto> createSerial(@RequestParam String serial) {
        ProductDetail detail = new ProductDetail();
        detail.setSerialNumber(serial);
        detail.setStatus(SerialStatus.INBOUND); // trạng thái ban đầu
        detail.setBin(null);                // chưa gán bin
        detail.setProduct(null);
        detail.setCreatedAt(LocalDateTime.now());
        detail.setUpdatedAt(LocalDateTime.now());
        _productDetailRepository.save(detail);
        var result = _productDetailMapper.toResponse(detail);
        return ApiResp.<ProductDetailResponseDto>builder().result(result).build();
    }

    // Get by serial number
    @GetMapping("/by-serial")
    public ApiResp<ProductDetailResponseDto> getBySerial(@RequestParam String serial) {
        ProductDetail detail = _productDetailRepository.findBySerialNumber(serial)
                .orElseThrow(() -> new EntityNotFoundException("ProductDetail not found: " + serial));
        return ApiResp.<ProductDetailResponseDto>builder()
                .result(_productDetailMapper.toResponse(detail))
                .build();
    }
}
