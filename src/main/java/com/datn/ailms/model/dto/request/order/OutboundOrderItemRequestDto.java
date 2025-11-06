package com.datn.ailms.model.dto.request.order;

import com.datn.ailms.model.entities.product_entities.ProductDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundOrderItemRequestDto {
//    UUID id;
    UUID productId;
    Integer orderQuantity;
    Integer scannedQuantity = 0;// để tạm vi phần này chưa có
//    String productName;
//    List<ProductDetail> productDetails = List.of(); // để tạm
}
