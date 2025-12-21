package com.datn.ailms.controllers;

import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.InventorySummaryDto;
import com.datn.ailms.services.InventoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final InventoryReportService service;

    @GetMapping("/inventory-summary")
    public ApiResp<Page<InventorySummaryDto>> getInventorySummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam UUID warehouseId,

            @RequestParam(required = false) UUID productId,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<InventorySummaryDto> data =
                service.getInventorySummary(
                        startDate,
                        endDate,
                        warehouseId,
                        productId,
                        page,
                        size
                );

        return ApiResp.<Page<InventorySummaryDto>>builder()
                .code(0)
                .message("success")
                .result(data)
                .build();
    }
}
