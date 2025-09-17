package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateAisleRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.AisleResponseDto;
import com.datn.ailms.model.entities.topo_entities.Aisle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AisleMapper {
    Aisle toAisle(CreateAisleRequestDto request);
    AisleResponseDto toAisleResponseDto(Aisle aisle);
    List<AisleResponseDto> toAisleResponseDtoList(List<Aisle> aisles);
}
