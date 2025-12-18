package com.datn.ailms.model.dto.request.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderItemRequestDto {
    UUID productId;      // FE chỉ gửi productId
    Integer orderQuantity;
}
