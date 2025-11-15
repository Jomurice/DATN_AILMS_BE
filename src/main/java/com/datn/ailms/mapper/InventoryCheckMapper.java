package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckRequestDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckItemResponseDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckResponseDto;
import com.datn.ailms.model.entities.inventory_entities.InventoryCheck;
import com.datn.ailms.model.entities.inventory_entities.InventoryCheckItem;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InventoryCheckMapper {

    // Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "checkedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", constant = "DRAFT")
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "code", ignore = true)
    InventoryCheck toEntity(InventoryCheckRequestDto dto);

    // Entity -> Response
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "createdBy.username", target = "createdByName")
    @Mapping(source = "checkedBy.id", target = "checkedBy")
    @Mapping(source = "checkedBy.username", target = "checkedByName")
    InventoryCheckResponseDto toResponse(InventoryCheck entity);

    List<InventoryCheckResponseDto> toResponseList(List<InventoryCheck> entities);

    // Item Entity -> Item Response
    @Mapping(source = "productDetail.id", target = "productDetailId")
    @Mapping(source = "productDetail.product.sku", target = "productSku")
    InventoryCheckItemResponseDto toItemResponse(InventoryCheckItem entity);

    // Helper mapping cho quan hệ
    default Warehouse map(UUID id) {
        if (id == null) return null;
        return Warehouse.builder().id(id).build();
    }
}