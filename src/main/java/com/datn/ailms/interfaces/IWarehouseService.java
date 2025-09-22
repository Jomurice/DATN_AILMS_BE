package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.warehouse_request.CreateWarehouseRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateWarehouseRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;

import java.util.List;
import java.util.UUID;

public interface IWarehouseService {

    // Tạo mới warehouse (zone/aisle/shelf/bin)
    WarehouseResponseDto create(CreateWarehouseRequestDto dto);

    // Cập nhật warehouse
    WarehouseResponseDto update(UUID id, UpdateWarehouseRequestDto dto);

    // Xoá warehouse
    void delete(UUID id);

    // Lấy theo id
    WarehouseResponseDto findById(UUID id);

    // Lấy tất cả warehouse
    List<WarehouseResponseDto> findAll();

    // Lấy tree warehouse theo location (zone → aisle → shelf → bin)
    List<WarehouseResponseDto> findTreeByLocation(UUID  locationId);

    // Lấy con trực tiếp
    List<WarehouseResponseDto> findChildren(UUID parentId);
}
