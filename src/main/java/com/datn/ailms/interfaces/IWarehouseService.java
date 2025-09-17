package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;

import java.util.List;
import java.util.UUID;

public interface IWarehouseService {

    List<WarehouseResponseDto> getAllWarehouses();

    WarehouseResponseDto getWarehouseById(UUID warehouseId);

    WarehouseResponseDto createWarehouse(CreateWarehouseRequestDto request);

    WarehouseResponseDto updateWarehouse(UUID warehouseId, UpdateWarehouseRequestDto request);
}
