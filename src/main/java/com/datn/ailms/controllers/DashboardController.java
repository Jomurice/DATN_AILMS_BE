package com.datn.ailms.controllers;


import com.datn.ailms.model.dto.request.DashboardStatsRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.DashboardStatsResponseDto;
import com.datn.ailms.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
