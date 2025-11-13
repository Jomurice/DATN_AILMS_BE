package com.datn.ailms.controllers.order_controllers;

import com.datn.ailms.interfaces.ISupplierService;
import com.datn.ailms.model.dto.request.order.SupplierRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.order.SupplierResponseDto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")

public class SupplierController {

    @Autowired
    private ISupplierService supplierService;

    @PostMapping
    public ApiResp<SupplierResponseDto> createSupplier(@RequestBody SupplierRequestDto supplierRequest) {
        SupplierResponseDto createSupplier = supplierService.createSupplier(supplierRequest);
        return ApiResp.<SupplierResponseDto>builder()
                .result(createSupplier)
                .build();
    }


    @PutMapping("/{supplierId}")
    public ApiResp<SupplierResponseDto> updateSupplier(@PathVariable UUID supplierId, @RequestBody SupplierRequestDto supplierRequest) {
        SupplierResponseDto updateSupplier = supplierService.updateSupplier(supplierId, supplierRequest);
        return ApiResp.<SupplierResponseDto>builder()
                .result(updateSupplier)
                .build();
    }


    @DeleteMapping("/{supplierId}")
    public ApiResp<Void> deleteSupplier(@PathVariable UUID supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ApiResp.<Void>builder()
                .message("Supplier deleted successfully")
                .build();
    }


    @GetMapping("/{supplierId}")
    public ApiResp<SupplierResponseDto> getSupplierById(@PathVariable UUID supplierId) {
        SupplierResponseDto supplier = supplierService.getSupplierById(supplierId);
        return ApiResp.<SupplierResponseDto>builder()
                .result(supplier)
                .build();
    }

    @PatchMapping("/{supplierId}/toggle-status")
    public ApiResp<SupplierResponseDto> toggleStatus(@PathVariable UUID supplierId) {
        SupplierResponseDto supplier = supplierService.toggleSupplierStatus(supplierId);
        return ApiResp.<SupplierResponseDto>builder().message("Supplier status changed successfully")
                .result(supplier).build();
    }

    @GetMapping
    public ApiResp<Page<SupplierResponseDto>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) Boolean active
    ) {
        Page<SupplierResponseDto> supplier = supplierService.getSuppliers(page, size, companyName, active);
        return ApiResp.<Page<SupplierResponseDto>>builder()
                .message("Get users successfully")
                .result(supplier)
                .build();
    }
}
