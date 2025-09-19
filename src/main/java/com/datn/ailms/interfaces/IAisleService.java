package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.warehouse_request.CreateAisleRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateAisleRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.AisleResponseDto;
import com.datn.ailms.model.entities.topo_entities.Aisle;

import java.util.List;
import java.util.UUID;

public interface IAisleService {

    List<AisleResponseDto> getAllAisles();
    List<AisleResponseDto> findAllByZoneIdNativeQuery(UUID zoneId);
    AisleResponseDto getAisleById(UUID aisleId);
    AisleResponseDto createAisle(CreateAisleRequestDto request);
    AisleResponseDto updateAisle(UUID aisleId,UpdateAisleRequestDto request);
}
