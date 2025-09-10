package com.datn.ailms.controllers.product_inventory;

import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.services.topoServices.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService _inventoryService;

    @PostMapping("/scan")
    ApiResp<ProductDetailResponseDto> scan(@RequestParam String serial){
        var result = _inventoryService.scanAndPutToBin(serial);
        return ApiResp.<ProductDetailResponseDto>builder().result(result).build();
    }

    @PostMapping("/move")
    ApiResp<ProductDetailResponseDto> move(@RequestParam String serial, @RequestParam UUID binId) {
       var result = _inventoryService.moveSerialToBin(serial,binId);
       return ApiResp.<ProductDetailResponseDto>builder().result(result).build();
    }

    @GetMapping("/locate")
     ApiResp<String> locate(@RequestParam String serial) {
        var result = _inventoryService.locate(serial);
        return ApiResp.<String>builder().result(result).build();
    }
}
