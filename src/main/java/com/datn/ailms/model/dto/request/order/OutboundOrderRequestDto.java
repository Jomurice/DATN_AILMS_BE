package com.datn.ailms.model.dto.request.order;

import com.datn.ailms.model.entities.product_entities.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundOrderRequestDto {
    String code;
    UUID customerId;
    UUID createdBy;
    String status;
    UUID warehouseId;
    List<OutboundOrderItemRequestDto> items;
}
