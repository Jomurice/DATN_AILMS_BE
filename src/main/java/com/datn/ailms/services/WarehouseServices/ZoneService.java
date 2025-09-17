package com.datn.ailms.services.WarehouseServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IZoneService;
import com.datn.ailms.mapper.ZoneMapper;
import com.datn.ailms.model.dto.request.warehouse_request.CreateZoneRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateZoneRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.ZoneResponseDto;
import com.datn.ailms.model.entities.topo_entities.Zone;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import com.datn.ailms.repositories.warehousetopology.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ZoneService implements IZoneService {
    private final ZoneRepository _zoneRepository;
    private final ZoneMapper _zoneMapper;
    @Override
    public List<ZoneResponseDto> getAllZones() {
        List<Zone> zones = _zoneRepository.findAll();
        return _zoneMapper.toZoneResponseDtoList(zones);
    }

    @Override
    public ZoneResponseDto getZoneById(UUID zoneId) {
        Zone zone = _zoneRepository.findById(zoneId).orElseThrow(
                ()-> new AppException(ErrorCode.ZONE_NOT_EXISTED)
        );
        return _zoneMapper.toZoneResponseDto(zone);
    }

    @Override
    public ZoneResponseDto createZone(CreateZoneRequestDto request) {

        Zone zone = _zoneMapper.toZone(request);

        zone.setCreatedAt(LocalDateTime.now());
        zone.setUpdatedAt(LocalDateTime.now());

        return  _zoneMapper.toZoneResponseDto(_zoneRepository.save(zone));
    }

    @Override
    public ZoneResponseDto updateZone(UUID zoneId, UpdateZoneRequestDto request) {
        Zone zone = _zoneRepository.findById(zoneId).orElseThrow(
                () -> new AppException(ErrorCode.ZONE_NOT_EXISTED)
        );
        zone.setCode(request.getCode());
        zone.setName(request.getName());
        zone.setUpdatedAt(LocalDateTime.now());

        return _zoneMapper.toZoneResponseDto(_zoneRepository.save(zone));
    }
}
