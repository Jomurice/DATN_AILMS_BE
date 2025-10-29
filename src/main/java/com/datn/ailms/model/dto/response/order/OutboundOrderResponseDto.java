package com.datn.ailms.model.dto.response.order;

import com.datn.ailms.model.entities.product_entities.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundOrderResponseDto {
    String code;
    LocalDate createAt;
    UUID createdBy;
    String customer;
    String status;
//    double totalPrice;
    List<OutboundOrderItemResponseDto> items;
}
