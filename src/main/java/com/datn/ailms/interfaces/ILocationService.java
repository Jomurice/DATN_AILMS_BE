package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.warehouse_request.CreateLocationRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.LocationResponseDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseResponseDto;
import com.datn.ailms.model.entities.Location;

import java.util.List;
import java.util.UUID;

public interface ILocationService {
    // Tạo mới location
    LocationResponseDto create(CreateLocationRequestDto dto);

    // Cập nhật location
    LocationResponseDto update(UUID id, CreateLocationRequestDto dto);

    // Xoá location
    void delete(UUID id);

    // Lấy theo id
    LocationResponseDto findById(UUID id);

    // Lấy tất cả
    List<LocationResponseDto> findAll();

    // Lấy tree (root + con)
    List<LocationResponseDto> findTree();

    // Lấy con trực tiếp
    List<LocationResponseDto> findChildren(UUID parentId);

    LocationResponseDto findLocationByWarehouseId(UUID warehouseId); // chỉ 1 location


    // Trả về danh sách warehouse theo locationId
    List<WarehouseResponseDto> findWarehousesByLocationId(UUID locationId);
}
