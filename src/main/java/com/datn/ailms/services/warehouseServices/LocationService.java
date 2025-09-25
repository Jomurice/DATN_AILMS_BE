package com.datn.ailms.services.warehouseServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.ILocationService;
import com.datn.ailms.mapper.LocationMapper;
import com.datn.ailms.mapper.WarehouseMapper;
import com.datn.ailms.model.dto.request.warehouse_request.CreateLocationRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.LocationResponseDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;
import com.datn.ailms.model.entities.Location;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.repositories.LocationRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService implements ILocationService {

    private final LocationRepository _locationRepository;
    private final LocationMapper _locationMapper;
    private final WarehouseRepository _warehouseRepository;
    private final WarehouseMapper _warehouseMapper;

    @Override
    public LocationResponseDto create(CreateLocationRequestDto dto) {
        Location parent = null;
        if (dto.getParentId() != null) {
            parent = _locationRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent location not found: " + dto.getParentId()));
        }

        Location location = _locationMapper.toEntity(dto);
        location.setParent(parent);

        return _locationMapper.toResponse(_locationRepository.save(location));
    }

    @Override
    public LocationResponseDto update(UUID id, CreateLocationRequestDto dto) {
        Location location = _locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found: " + id));
        location.setName(dto.getName());
        location.setAddress(dto.getAddress());

        if (dto.getParentId() != null) {
            Location parent = _locationRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent location not found: " + dto.getParentId()));
            location.setParent(parent);
        } else {
            location.setParent(null);
        }

        return _locationMapper.toResponse(_locationRepository.save(location));
    }

    @Override
    public void delete(UUID id) {
        if (!_locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location not found: " + id);
        }
        _locationRepository.deleteById(id);
    }

    @Override
    public LocationResponseDto findById(UUID id) {
        Location location = _locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found: " + id));
        return _locationMapper.toResponse(location);
    }

    @Override
    public List<LocationResponseDto> findAll() {
        return _locationMapper.toResponseList(_locationRepository.findAll());
    }

    @Override
    public List<LocationResponseDto> findTree() {
        List<Location> roots = _locationRepository.findByParentIsNull();
        return _locationMapper.toResponseList(roots);
    }

    @Override
    public List<LocationResponseDto> findChildren(UUID parentId) {
        List<Location> children = _locationRepository.findByParentId(parentId);
        return _locationMapper.toResponseList(children);
    }

    @Override
    public LocationResponseDto findLocationByWarehouseId(UUID warehouseId) {
        Warehouse warehouse = _warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));

        return _locationMapper.toResponse(warehouse.getLocation());
    }

    @Override
    public List<WarehouseResponseDto> findWarehousesByLocationId(UUID locationId) {
        List<Warehouse> warehouses = _warehouseRepository.findByLocationId(locationId);
        return _warehouseMapper.toResponseList(warehouses);
    }


}
