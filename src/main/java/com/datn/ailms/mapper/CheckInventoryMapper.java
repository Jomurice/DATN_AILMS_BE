package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryItemRequestDTO;
import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryRequestDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryItemResponseDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryResponseDTO;
import com.datn.ailms.model.entities.checkInventory_entities.CheckInventory;
import com.datn.ailms.model.entities.checkInventory_entities.CheckInventoryItem;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {WarehouseMapper.class})  // Uses WarehouseMapper cho nested warehouse
public interface CheckInventoryMapper {

    // toEntity: Từ RequestDTO sang Entity (status mặc định, warehouseId → warehouse.id)
    @Mapping(target = "status", constant = "UNCHECKED")  // Mặc định UNCHECKED nếu null
    @Mapping(target = "createdAt", ignore = true)  // Ignore timestamps (set bằng @PrePersist)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productDetails", ignore = true)  // Set từ serialNumbers trong Service
    @Mapping(target = "items", ignore = true)  // Tạo trong Service
    @Mapping(target = "warehouse.id", source = "warehouseId")  // Map warehouseId → warehouse.id
    CheckInventory toEntity(CheckInventoryRequestDTO request);

    // toResponse: Từ Entity sang ResponseDTO (map warehouse.id → warehouseId, warehouse.name → warehouseName)
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(source = "items", target = "items", qualifiedByName = "mapItems")  // Map items sang ItemResponse list
    CheckInventoryResponseDTO toResponse(CheckInventory entity);

    List<CheckInventoryResponseDTO> toResponseList(List<CheckInventory> entities);

    // Mapper cho Item
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CheckInventoryItem toItemEntity(CheckInventoryItemRequestDTO request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CheckInventoryItemResponseDTO toItemResponse(CheckInventoryItem entity);

    List<CheckInventoryItemResponseDTO> toItemResponseList(List<CheckInventoryItem> entities);

    // Qualified method cho map items (nested mapping)
    @Named("mapItems")
    default Set<CheckInventoryItemResponseDTO> mapItems(Set<CheckInventoryItem> items) {
        return items.stream()
                .map(this::toItemResponse)
                .collect(Collectors.toSet());
    }

    // @AfterMapping để handle serialNumbers nếu cần (tùy chọn, nhưng để Service xử lý)
    @AfterMapping
    default void setProductDetailsFromSerials(@MappingTarget CheckInventory entity, CheckInventoryRequestDTO request) {
        if (request.getSerialNumbers() != null) {
            // Logic tìm ProductDetail từ serial (có thể inject repo, nhưng để Service)
            // Ví dụ: entity.getProductDetails().addAll(findFromSerials(request.getSerialNumbers()));
        }
    }
}