package com.datn.ailms.controllers;

import com.datn.ailms.services.StockService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockController {

    final StockService _stockservice;

    @GetMapping("/{productId}")
    public long getStock(@PathVariable UUID productId){
        return _stockservice.getAvailableQuantity(productId);
    }
}
