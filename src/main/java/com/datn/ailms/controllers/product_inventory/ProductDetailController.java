package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.ProductDetail;
import com.datn.ailms.model.entities.SerialStatus;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailRepository _productDetailRepository;
    private final ProductDetailMapper _productDetailMapper;
    // API tạo 1 serial mới chưa gán bin
    @PostMapping("/create-serial")
    public ApiResp<ProductDetailResponseDto> createSerial(@RequestParam String serial) {
        ProductDetail detail = new ProductDetail();
        detail.setSerialNumber(serial);
        detail.setStatus(SerialStatus.INBOUND); // trạng thái ban đầu
        detail.setBin(null);                // chưa gán bin

        _productDetailRepository.save(detail);
        var result = _productDetailMapper.toResponse(detail);
        return ApiResp.<ProductDetailResponseDto>builder().result(result).build();
    }
}
