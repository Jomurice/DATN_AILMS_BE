package com.datn.ailms.services;

import com.datn.ailms.interfaces.IDashboardService;
import com.datn.ailms.model.dto.InOutSeriesDto;
import com.datn.ailms.model.dto.request.DashboardStatsRequestDto;
import com.datn.ailms.model.dto.response.DashboardStatsResponseDto;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.repositories.DashboardRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final ProductDetailRepository _productDetailRepo;
    private final UserRepository _userRepo;
    private final DashboardRepository dashboardRepository;

    @Override
    public DashboardStatsResponseDto getDashboardStats(DashboardStatsRequestDto requestDto) {
        UUID warehouseId = requestDto.getWarehouseId();

        long inStock;
        if (warehouseId != null) {
            inStock = _productDetailRepo.countByStatusAndWarehouseId(SerialStatus.IN_WAREHOUSE, warehouseId);
        } else {
            inStock = _productDetailRepo.countByStatus(SerialStatus.IN_WAREHOUSE);
        }

        long employees = _userRepo.countUsers();

        return DashboardStatsResponseDto.builder()
                .inStock(inStock)
                .employees(employees)
                .build();
    }

    @Override
    public InOutSeriesDto getInboundOutboundSeries(LocalDateTime start, LocalDateTime end, String timeframe, UUID warehouseId) {
        List<Object[]> inboundRows;
        List<Object[]> outboundRows;

        // Trường hợp có warehouse cụ thể
        if (warehouseId == null) {
            switch (timeframe) {
                case "24H":
                    inboundRows = dashboardRepository.countInboundByHour(start, end);
                    outboundRows = dashboardRepository.countOutboundByHour(start, end);
                    break;
                case "7D":
                case "30D":
                case "1M":
                    inboundRows = dashboardRepository.countInboundByDay(start, end);
                    outboundRows = dashboardRepository.countOutboundByDay(start,end);
                    break;
                case "1Y":
                default:
                    inboundRows = dashboardRepository.countInboundByMonth(start, end);
                    outboundRows = dashboardRepository.countOutboundByMonth(start,end);
                    break;
            }
        }
        // Trường hợp không chọn warehouse
        else {
            switch (timeframe) {
                case "24H":
                    inboundRows = dashboardRepository.countInboundByHourAndWarehouse(start, end,warehouseId);
                    outboundRows = dashboardRepository.countOutboundByHourAndWarehouse(start,end,warehouseId);
                    break;
                case "7D":
                case "30D":
                case "1M":
                    inboundRows = dashboardRepository.countInboundByDayAndWarehouse(start, end,warehouseId);
                    outboundRows = dashboardRepository.countOutboundByDayAndWarehouse(start, end,warehouseId);
                    break;
                case "1Y":
                default:
                    inboundRows = dashboardRepository.countInboundByMonthAndWarehouse(start, end,warehouseId);
                    outboundRows = dashboardRepository.countOutboundByMonthAndWarehouse(start, end,warehouseId);
                    break;
            }
        }

        // Gom dữ liệu theo key (ngày, giờ, tháng)
        Map<String, Long> inboundMap = inboundRows.stream()
                .collect(Collectors.toMap(r -> (String) r[0], r -> (Long) r[1]));
        Map<String, Long> outboundMap = outboundRows.stream()
                .collect(Collectors.toMap(r -> (String) r[0], r -> (Long) r[1]));

        List<String> labels = new ArrayList<>();
        List<Long> inbound = new ArrayList<>();
        List<Long> outbound = new ArrayList<>();

        switch (timeframe) {
            case "24H":
                LocalDateTime cursorHour = start.withMinute(0).withSecond(0).withNano(0);
                while (!cursorHour.isAfter(end)) {
                    String key = cursorHour.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
                    labels.add(key);
                    inbound.add(inboundMap.getOrDefault(key, 0L));
                    outbound.add(outboundMap.getOrDefault(key, 0L));
                    cursorHour = cursorHour.plusHours(1);
                }
                break;

            case "7D":
            case "30D":
            case "1M":
                LocalDate cursorDay = start.toLocalDate();
                LocalDate endDay = end.toLocalDate();
                while (!cursorDay.isAfter(endDay)) {
                    String key = cursorDay.toString();
                    labels.add(key);
                    inbound.add(inboundMap.getOrDefault(key, 0L));
                    outbound.add(outboundMap.getOrDefault(key, 0L));
                    cursorDay = cursorDay.plusDays(1);
                }
                break;

            case "1Y":
            default:
                LocalDateTime cursorMonth = start.withDayOfMonth(1);
                while (!cursorMonth.isAfter(end)) {
                    String key = cursorMonth.getYear() + "-" + String.format("%02d", cursorMonth.getMonthValue());
                    labels.add(key);
                    inbound.add(inboundMap.getOrDefault(key, 0L));
                    outbound.add(outboundMap.getOrDefault(key, 0L));
                    cursorMonth = cursorMonth.plusMonths(1);
                }
                break;
        }

        return InOutSeriesDto.builder()
                .labels(labels)
                .inbound(inbound)
                .outbound(outbound)
                .build();
    }
}
