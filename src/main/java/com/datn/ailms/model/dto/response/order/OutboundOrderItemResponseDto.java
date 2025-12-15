package com.datn.ailms.model.dto.response.order;

import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutboundOrderItemResponseDto {
    private UUID id;
    private Integer orderQuantity;

    private Integer scannedQuantity;

    private ProductResponseDto product;
    List<ProductDetailResponseDto> productDetails;
}
