package com.datn.ailms.services;

import com.datn.ailms.interfaces.IStockRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockService {

    final ProductDetailRepository _productDetailRepo;

    public long getAvailableQuantity(UUID productId) {
        return _productDetailRepo.countAvailableByProductId(productId);
    }
}
