package com.datn.ailms.services;

import com.datn.ailms.interfaces.IDashboardService;
import com.datn.ailms.model.dto.request.DashboardStatsRequestDto;
import com.datn.ailms.model.dto.response.DashboardStatsResponseDto;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final ProductDetailRepository _productDetailRepo;
    private final UserRepository _userRepo;

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
}
