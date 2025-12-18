package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.order.PurchaseOrderRequestDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import com.datn.ailms.model.entities.order_entites.Supplier;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",uses = { PurchaseOrderItemMapper.class })
public interface PurchaseOrderMapper {
    @Mapping(source = "items", target = "items")
    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "supplier.companyName", target = "supplier")
    PurchaseOrderResponseDto toDto(PurchaseOrder entity);

    @Mapping(source = "items", target = "items")
    @Mapping(source = "createdBy", target = "createdBy.id")
    @Mapping(source = "warehouseId", target = "warehouse.id")
    @Mapping(source = "supplierId", target = "supplier")
    PurchaseOrder toEntity(PurchaseOrderRequestDto dto);

    List<PurchaseOrderResponseDto> toResponseList(List<PurchaseOrder> entities);

//    List<PurchaseOrder> toEntityList(List<PurchaseOrderResponseDto> dtos);
default User mapUser(UUID id) {
    if (id == null) return null;
    User user = new User();
    user.setId(id);
    return user;
}

    default Warehouse mapWarehouse(UUID id) {
        if (id == null) return null;
        Warehouse w = new Warehouse();
        w.setId(id);
        return w;
    }

    default Supplier map(UUID supplierId) {
        if (supplierId == null) return null;
        Supplier s = new Supplier();
        s.setId(supplierId);
        return s;
    }

    default String map(Supplier supplier) {
        if (supplier == null) return null;
        return supplier.getCompanyName();
    }

}
