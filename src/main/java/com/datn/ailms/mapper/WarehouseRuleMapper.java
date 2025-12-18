package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.WarehouseRuleRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseRuleResponseDto;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.model.entities.topo_entities.WarehouseRule;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseRuleMapper {

    // Map từ RequestDto -> Entity
    @Mapping(source = "warehouseId", target = "warehouse.id")
    WarehouseRule toEntity(WarehouseRuleRequestDto dto);

    // Map từ Entity -> ResponseDto
    @Mapping(source = "warehouse.id", target = "warehouseId")
    WarehouseRuleResponseDto toResponse(WarehouseRule entity);

    List<WarehouseRuleResponseDto> toResponseList(List<WarehouseRule> entities);
}
