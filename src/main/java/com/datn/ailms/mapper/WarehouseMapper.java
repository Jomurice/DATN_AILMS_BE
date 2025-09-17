package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface WarehouseMapper {
    Warehouse toWarehouse(CreateWarehouseRequestDto warehouse);
    WarehouseResponseDto toWarehouseResponseDto(Warehouse warehouse);
    List<WarehouseResponseDto> toWarehouseResponseDtoList(List<Warehouse> warehouses);

}
