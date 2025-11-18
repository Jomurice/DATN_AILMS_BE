package com.datn.ailms.interfaces;


import com.datn.ailms.model.dto.request.order.SupplierRequestDto;
import com.datn.ailms.model.dto.response.order.SupplierResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;


public interface ISupplierService {

    Page<SupplierResponseDto> getSuppliers(int page, int size, String companyName, Boolean active);

    List<SupplierResponseDto> getAllSuppliers();
    SupplierResponseDto getSupplierById(UUID supplierId);
    SupplierResponseDto createSupplier(SupplierRequestDto supplierRequest);
    SupplierResponseDto updateSupplier(UUID supplierId, SupplierRequestDto supplierRequest);
    void deleteSupplier(UUID supplierId);

    SupplierResponseDto toggleSupplierStatus(UUID supplierId);
}
