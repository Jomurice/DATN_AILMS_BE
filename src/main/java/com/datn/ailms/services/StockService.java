package com.datn.ailms.services;

import com.datn.ailms.interfaces.IStockRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockService {

    final IStockRepository _stockRepo;

    public StockService(IStockRepository stockRepository) {
        this._stockRepo = stockRepository;
    }

    public int getAvailableQuantity(UUID productId) {
        return _stockRepo.findQuantity(productId).orElse(0);
    }
}
