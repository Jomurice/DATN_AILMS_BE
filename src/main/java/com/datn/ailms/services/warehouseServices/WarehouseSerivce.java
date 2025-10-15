package com.datn.ailms.services.warehouseServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IWarehouseService;
import com.datn.ailms.mapper.WarehouseMapper;
import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
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
public class WarehouseSerivce implements IWarehouseService {

    private final WarehouseRepository _warehouseRepository;
    private final WarehouseMapper _warehouseMapper;

    @Override
    public WarehouseResponseDto create(CreateWarehouseRequestDto dto) {
        Warehouse parent = null;

        // Nếu có parentId
        if (dto.getParentId() != null) {
            parent = _warehouseRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent warehouse not found: " + dto.getParentId()));
            // Nếu không truyền locationId → lấy location từ parent
            if (dto.getLocation() == null) {
                dto.setLocation(parent.getLocation());
            }
        } else {
            // Warehouse gốc → bắt buộc có locationId
//            if (dto.getLocation() == null) {
//                throw new AppException(ErrorCode.LOCATION_NOT_FOUND);
//            }
        }

        Warehouse warehouse = _warehouseMapper.toEntity(dto);
//        warehouse.setLocation(dto.getLo);
        warehouse.setParent(parent);

        return _warehouseMapper.toResponse(_warehouseRepository.save(warehouse));
    }

    @Override
    public WarehouseResponseDto update(UUID id, UpdateWarehouseRequestDto dto) {
        Warehouse warehouse = _warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found: " + id));

        warehouse.setName(dto.getName());
        warehouse.setCode(dto.getCode());
        warehouse.setType(dto.getType());
        warehouse.setLocation(dto.getLocation());

        if (dto.getParentId() != null) {
            Warehouse parent = _warehouseRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent warehouse not found: " + dto.getParentId()));
            warehouse.setParent(parent);
        } else {
            warehouse.setParent(null);
        }

        return _warehouseMapper.toResponse(_warehouseRepository.save(warehouse));
    }

    @Override
    public void delete(UUID id) {
        if (!_warehouseRepository.existsById(id)) {
            throw new EntityNotFoundException("Warehouse not found: " + id);
        }
        _warehouseRepository.deleteById(id);
    }

    @Override
    public WarehouseResponseDto findById(UUID id) {
        Warehouse warehouse = _warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found: " + id));
        return _warehouseMapper.toResponse(warehouse);
    }

    @Override
    public List<WarehouseResponseDto> findAll() {
        return _warehouseMapper.toResponseList(_warehouseRepository.findAll());
    }

    @Override
    public List<WarehouseResponseDto> findTreeByLocation(String  location) {
        List<Warehouse> roots = _warehouseRepository.findByLocationAndParentIsNull(location);
        return _warehouseMapper.toResponseList(roots);
    }

    @Override
    public List<WarehouseResponseDto> findChildren(UUID parentId) {
        List<Warehouse> children = _warehouseRepository.findByParentId(parentId);
        return _warehouseMapper.toResponseList(children);
    }
}
