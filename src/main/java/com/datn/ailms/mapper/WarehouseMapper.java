package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;
import com.datn.ailms.model.entities.Location;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse toEntity(CreateWarehouseRequestDto dto);

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "children", target = "children")
    WarehouseResponseDto toResponse(Warehouse entity);

    List<WarehouseResponseDto> toResponseList(List<Warehouse> entities);
}
