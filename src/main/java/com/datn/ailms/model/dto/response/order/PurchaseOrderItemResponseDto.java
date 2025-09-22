package com.datn.ailms.model.dto.response.order;

import com.datn.ailms.model.dto.response.UserResponseDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderItemResponseDto {
    private UUID id;
    private Integer orderQuantity;

    private Integer scannedQuantity;

    private ProductResponseDto product;
    List<ProductDetailResponseDto> scannedSerials;
}
