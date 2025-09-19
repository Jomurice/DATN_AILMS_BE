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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseService implements IWarehouseService {

    private final WarehouseRepository _warehouseRepository;
    private final WarehouseMapper _warehouseMapper;


    @Override
    public List<WarehouseResponseDto> getAllWarehouses() {
        List<Warehouse> warehouses = _warehouseRepository.findAll();
        return _warehouseMapper.toWarehouseResponseDtoList(warehouses);
    }

    @Override
    public WarehouseResponseDto getWarehouseById(UUID warehouseId) {
        Warehouse warehouse = _warehouseRepository.findById(warehouseId).orElseThrow(
                ()-> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED)
        );
        return _warehouseMapper.toWarehouseResponseDto(warehouse);
    }

    @Override
    public WarehouseResponseDto createWarehouse(CreateWarehouseRequestDto request) {
         Warehouse warehouse = _warehouseMapper.toWarehouse(request);

         warehouse.setCreatedAt(LocalDateTime.now());
         warehouse.setUpdatedAt(LocalDateTime.now());

         warehouse = _warehouseRepository.save(warehouse);

         return _warehouseMapper.toWarehouseResponseDto(warehouse);
    }

    @Override
    public WarehouseResponseDto updateWarehouse(UUID warehouseId, UpdateWarehouseRequestDto request) {
        Warehouse warehouse = _warehouseRepository.findById(warehouseId).orElseThrow(
                () -> new AppException (ErrorCode.WAREHOUSE_NOT_EXISTED)
        );
        warehouse.setCode(request.getCode());
        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setUpdatedAt(LocalDateTime.now());

        warehouse = _warehouseRepository.save(warehouse);
        return _warehouseMapper.toWarehouseResponseDto(warehouse);
    }
}
