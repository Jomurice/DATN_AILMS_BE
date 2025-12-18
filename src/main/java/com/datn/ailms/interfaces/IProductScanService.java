package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.inventory.ProductGenerateSerialRequest;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;

import java.util.List;

public interface IProductScanService {
    List<ProductDetailResponseDto> generateSerials(ProductGenerateSerialRequest request);
}
