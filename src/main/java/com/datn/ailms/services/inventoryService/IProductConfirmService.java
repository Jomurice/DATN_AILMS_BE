package com.datn.ailms.services.inventoryService;

import com.datn.ailms.model.dto.request.inventory.ProductConfirmRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;

public interface IProductConfirmService {
    ProductDetailResponseDto confirmSerial(ProductConfirmRequestDto request);

}
