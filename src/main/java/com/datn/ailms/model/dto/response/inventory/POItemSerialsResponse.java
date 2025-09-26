package com.datn.ailms.model.dto.response.inventory;

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
public class POItemSerialsResponse {
    private UUID poItemId;
    private UUID productId;
    private List<String> serialNumbers; // Serial tạo ra
}
