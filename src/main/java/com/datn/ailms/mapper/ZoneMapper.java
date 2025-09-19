package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateZoneRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateZoneRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.ZoneResponseDto;
import com.datn.ailms.model.entities.topo_entities.Zone;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;


@Mapper(componentModel = "spring")
public interface ZoneMapper {
    Zone toZone (CreateZoneRequestDto request);
    Zone toZone (UUID zoneId, UpdateZoneRequestDto request);
    ZoneResponseDto toZoneResponseDto (Zone zone);
    List<ZoneResponseDto> toZoneResponseDtoList (List<Zone> zones);
}
