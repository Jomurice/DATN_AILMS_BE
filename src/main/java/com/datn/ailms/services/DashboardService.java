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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final ProductDetailRepository _productDetailRepo;
    private final UserRepository _userRepo;
    private final DashboardRepository dashboardRepository;

    @Override
    public DashboardStatsResponseDto getDashboardStats(DashboardStatsRequestDto requestDto) {

        String timeframe = requestDto.getTimeframe();

        long inStock = _productDetailRepo.countProductDetail();

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

//        long exported = orderDetailRepository.countExportedBetween(start, now);
        long employees = _userRepo.countUsers();
        return DashboardStatsResponseDto.builder()
                .inStock(inStock)
//                .exported(exported)
                .employees(employees)
                .build();
    }

    @Override
    public InOutSeriesDto getInboundOutboundSeries(LocalDateTime start, LocalDateTime end, String timeframe) {
        List<Object[]> inboundRows;
        List<Object[]> outboundRows;

        // Chọn query phù hợp
        switch (timeframe) {
            case "24H":
                inboundRows = dashboardRepository.countByStatusAndHour(SerialStatus.INBOUND, start, end);
                outboundRows = dashboardRepository.countByStatusAndHour(SerialStatus.OUTBOUND, start, end);
                break;
            case "7D":
            case "30D":
            case "1M":
                inboundRows = dashboardRepository.countByStatusAndDay(SerialStatus.INBOUND, start, end);
                outboundRows = dashboardRepository.countByStatusAndDay(SerialStatus.OUTBOUND, start, end);
                break;
            case "1Y":
            default: // ALL
                inboundRows = dashboardRepository.countByStatusAndMonth(SerialStatus.INBOUND, start, end);
                outboundRows = dashboardRepository.countByStatusAndMonth(SerialStatus.OUTBOUND, start, end);
                break;
        }

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
                    String key = cursorDay.toString(); // yyyy-MM-dd
                    labels.add(key);
                    inbound.add(inboundMap.getOrDefault(key, 0L));
                    outbound.add(outboundMap.getOrDefault(key, 0L));
                    cursorDay = cursorDay.plusDays(1);
                }
                break;

            case "1Y":
            default: // ALL
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
