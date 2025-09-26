package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse toEntity(CreateWarehouseRequestDto dto);

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "children", target = "children")
    WarehouseResponseDto toResponse(Warehouse entity);

    List<WarehouseResponseDto> toResponseList(List<Warehouse> entities);
}
