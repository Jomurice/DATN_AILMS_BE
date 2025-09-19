package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.*;
import com.datn.ailms.model.entities.topo_entities.Aisle;
import com.datn.ailms.model.entities.topo_entities.Shelf;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.model.entities.topo_entities.Zone;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface WarehouseMapper {
    Warehouse toWarehouse(CreateWarehouseRequestDto warehouse);
    WarehouseResponseDto toWarehouseResponseDto(Warehouse warehouse);
    List<WarehouseResponseDto> toWarehouseResponseDtoList(List<Warehouse> warehouses);




    ZoneResponseDto toZoneDto(Zone zone);
    AisleResponseDto toAisleDto(Aisle aisle);
    ShelfResponseDto toShelfDto(Shelf shelf);
}
