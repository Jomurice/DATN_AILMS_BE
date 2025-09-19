package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.warehouse_request.CreateZoneRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateZoneRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.ZoneResponseDto;
import com.datn.ailms.model.entities.topo_entities.Zone;

import java.util.List;
import java.util.UUID;

public interface IZoneService {

    List<ZoneResponseDto> getAllZones();
    List<ZoneResponseDto> findAllByWarehouseIdNativeQuery(UUID warehouseId);
    ZoneResponseDto getZoneById(UUID zoneId);
    ZoneResponseDto createZone(CreateZoneRequestDto request);
    ZoneResponseDto updateZone(UUID zoneId, UpdateZoneRequestDto request);
}
