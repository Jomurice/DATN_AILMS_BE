package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.ISupplierService;
import com.datn.ailms.mapper.SupplierMapper;
import com.datn.ailms.model.dto.request.order.SupplierRequestDto;
import com.datn.ailms.model.dto.response.order.SupplierResponseDto;
import com.datn.ailms.model.entities.order_entites.Supplier;
import com.datn.ailms.repositories.orderRepo.SupplierRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierService implements ISupplierService {

    private SupplierRepository supplierRepo;
    private SupplierMapper supplierMapper;

    @Override
    public List<SupplierResponseDto> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepo.findAll();
        return supplierMapper.toSupplierResponseDtoList(suppliers);
    }

    @Override
    public SupplierResponseDto getSupplierById(UUID supplierId) {
        Supplier supplier = supplierRepo.findById(supplierId).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)
        );
        return supplierMapper.toSupplierResponseDto(supplier);
    }

    @Override
    public SupplierResponseDto createSupplier(SupplierRequestDto supplierRequest) {
        Supplier supplier = supplierMapper.toSupplier(supplierRequest);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setActive(true);

        supplierRepo.save(supplier);
        return supplierMapper.toSupplierResponseDto(supplier);
    }

    @Override
    public SupplierResponseDto updateSupplier(UUID supplierId, SupplierRequestDto supplierRequest) {
        Supplier supplier = supplierRepo.findById(supplierId).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)
        );

        supplier.setCompanyName(supplierRequest.getCompanyName());
        supplier.setContactName(supplierRequest.getContactName());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setPhone(supplierRequest.getPhone());
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setUpdatedAt(LocalDateTime.now());


        supplierRepo.save(supplier);
        return supplierMapper.toSupplierResponseDto(supplier);
    }

    @Override
    public void deleteSupplier(UUID supplierId) {
        Supplier supplier = supplierRepo.findById(supplierId).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)
        );
                supplierRepo.deleteById(supplierId);
    }

    @Override
    public SupplierResponseDto toggleSupplierStatus(UUID supplierId) {
        Supplier supplier = supplierRepo.findById(supplierId).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)
        );

        supplier.setActive(!supplier.isActive());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepo.save(supplier);

        return supplierMapper.toSupplierResponseDto(supplier);
    }

    @Override
    public Page<SupplierResponseDto> getSuppliers(int page, int size, String companyName, Boolean active) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("company_name").ascending());
        if (companyName == null) companyName = "";

        Page<Supplier> suppliers = supplierRepo.searchSuppliers(companyName, active, pageable);

        return suppliers.map(supplierMapper::toSupplierResponseDto);
    }

}
