package com.datn.ailms.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponseDto {
    private long inStock;    // số lượng sản phẩm còn tồn
    private long exported;   // số lượng sản phẩm đã xuất
    private long employees;  // số lượng nhân viên
}
