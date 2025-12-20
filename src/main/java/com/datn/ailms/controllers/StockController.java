package com.datn.ailms.controllers;

import com.datn.ailms.services.StockService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockController {

    final StockService _stockservice;

    @GetMapping
    public long getStock(@RequestParam UUID productId, @RequestParam UUID warehouseId){
        return _stockservice.getAvailableQuantity(productId,warehouseId);
    }
}
