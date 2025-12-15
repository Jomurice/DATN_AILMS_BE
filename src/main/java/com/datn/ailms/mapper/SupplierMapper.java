package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.order.SupplierRequestDto;
import com.datn.ailms.model.dto.response.order.SupplierResponseDto;
import com.datn.ailms.model.entities.order_entites.Supplier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface SupplierMapper {
    Supplier toSupplier(SupplierRequestDto supplierRequest);
    SupplierResponseDto toSupplierResponseDto(Supplier supplier);
    List<SupplierResponseDto> toSupplierResponseDtoList(List<Supplier> supplierList);
}
