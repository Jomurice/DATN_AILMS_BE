package com.datn.ailms.model.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboundOrderItemRequestDto {
    UUID productId;
    Integer orderQuantity;
}
