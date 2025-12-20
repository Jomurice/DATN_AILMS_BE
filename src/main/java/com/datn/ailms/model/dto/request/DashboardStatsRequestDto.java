package com.datn.ailms.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsRequestDto {
    private String timeframe;
    private UUID warehouseId;
    private String statusProduct;
}
