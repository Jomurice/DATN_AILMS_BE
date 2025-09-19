package com.datn.ailms.model.dto.request.order;

import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;
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
public class PurchaseOrderRequestDto {
    String code;
    String supplier;
    String status;
    LocalDate createdAt;

    List<PurchaseOrderItemRequestDto> items;
}
