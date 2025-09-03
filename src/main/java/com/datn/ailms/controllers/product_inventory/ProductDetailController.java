package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.ProductDetail;
import com.datn.ailms.model.entities.SerialStatus;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.services.inventoryService.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailRepository _productDetailRepository;
    private final ProductDetailMapper _productDetailMapper;
    private final ProductRepository productRepo;
    // API tạo 1 serial mới chưa gán bin
    @PostMapping("/create-serial")
    public ApiResp<ProductDetailResponseDto> createSerial(@RequestParam String serial,
                                                          @RequestParam UUID productId) {
        var product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDetail detail = new ProductDetail();
        detail.setSerialNumber(serial);
        detail.setStatus(SerialStatus.INBOUND); // trạng thái ban đầu
        detail.setBin(null);                // chưa gán bin
        detail.setProduct(product);

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
