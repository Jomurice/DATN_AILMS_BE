package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.DashboardStatsRequestDto;
import com.datn.ailms.model.dto.response.DashboardStatsResponseDto;

public interface IDashboardService {
    DashboardStatsResponseDto getDashboardStats(DashboardStatsRequestDto requestDto);
}
