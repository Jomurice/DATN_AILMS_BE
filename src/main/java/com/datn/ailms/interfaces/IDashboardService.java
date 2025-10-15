package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.InOutSeriesDto;
import com.datn.ailms.model.dto.request.DashboardStatsRequestDto;
import com.datn.ailms.model.dto.response.DashboardStatsResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface IDashboardService {
    DashboardStatsResponseDto getDashboardStats(DashboardStatsRequestDto requestDto);
    InOutSeriesDto getInboundOutboundSeries(LocalDateTime start, LocalDateTime end, String timeFrame, UUID warehouseId);
}
