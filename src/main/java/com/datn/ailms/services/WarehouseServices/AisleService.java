package com.datn.ailms.services.WarehouseServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IAisleService;
import com.datn.ailms.mapper.AisleMapper;
import com.datn.ailms.model.dto.request.warehouse_request.CreateAisleRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateAisleRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.AisleResponseDto;
import com.datn.ailms.model.entities.topo_entities.Aisle;
import com.datn.ailms.repositories.warehousetopology.AisleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AisleService implements IAisleService {

    private final AisleRepository _aisleRepository;
    private final AisleMapper _aisleMapper;


    @Override
    public List<AisleResponseDto> getAllAisles() {
        List<Aisle> aisles = _aisleRepository.findAll();
        return _aisleMapper.toAisleResponseDtoList(aisles);
    }

    @Override
    public List<AisleResponseDto> findAllByZoneIdNativeQuery(UUID zoneId) {
        List<Aisle> aisles = _aisleRepository.findAllByZoneIdNativeQuery(zoneId);
        return _aisleMapper.toAisleResponseDtoList(aisles);
    }

    @Override
    public AisleResponseDto getAisleById(UUID aisleId) {
        Aisle aisle = _aisleRepository.findById(aisleId).orElseThrow(
                ()-> new AppException(ErrorCode.AISLE_NOT_EXISTED)
        );
        return _aisleMapper.toAisleResponseDto(aisle);
    }

    @Override
    public AisleResponseDto createAisle(CreateAisleRequestDto request) {
        Aisle aisle = _aisleMapper.toAisle(request);
        aisle.setCreatedAt(LocalDateTime.now());
        aisle.setUpdatedAt(LocalDateTime.now());

        return _aisleMapper.toAisleResponseDto(_aisleRepository.save(aisle));
    }

    @Override
    public AisleResponseDto updateAisle(UUID aisleId,UpdateAisleRequestDto request) {
        Aisle aisle = _aisleRepository.findById(aisleId).orElseThrow(
            ()-> new AppException(ErrorCode.AISLE_NOT_EXISTED)
        );
        aisle.setCode(request.getCode());
        aisle.setName(request.getName());
        aisle.setUpdatedAt(LocalDateTime.now());

        return _aisleMapper.toAisleResponseDto(_aisleRepository.save(aisle));
    }
}
