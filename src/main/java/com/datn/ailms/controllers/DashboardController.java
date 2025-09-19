package com.datn.ailms.controllers;


import com.datn.ailms.model.dto.InOutSeriesDto;
import com.datn.ailms.model.dto.request.DashboardStatsRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.DashboardStatsResponseDto;
import com.datn.ailms.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService _dashboardService;

    @GetMapping("/stats")
    ApiResp<DashboardStatsResponseDto> getDashboardStat(@RequestParam(name="timeframe",defaultValue = "ALL") String timeframe){
        DashboardStatsRequestDto requestDto = DashboardStatsRequestDto.builder()
                .timeframe(timeframe)
                .build();
        var result = _dashboardService.getDashboardStats(requestDto);
        return ApiResp.<DashboardStatsResponseDto>builder().result(result).build();
    }

    @GetMapping("/series")
    public ApiResp<InOutSeriesDto> getSeries(
            @RequestParam(name = "timeframe", defaultValue = "30D") String timeframe
    ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        switch (timeframe) {
            case "24H":
                start = now.minusHours(24);
                break;
            case "7D":
                start = now.minusDays(7);
                break;
            case "30D":
                start = now.minusDays(30);
                break;
            case "1M":
                start = now.minusMonths(1);
                break;
            case "1Y":
                start = now.minusYears(1);
                break;
            default:
                start = now.minusYears(100); // ALL
                break;
        }

        InOutSeriesDto result = _dashboardService.getInboundOutboundSeries(
                start,
                now,
                timeframe
        );

        return ApiResp.<InOutSeriesDto>builder().result(result).build();
    }

}
