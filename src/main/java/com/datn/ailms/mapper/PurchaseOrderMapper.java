package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.order.PurchaseOrderRequestDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderResponseDto;
import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = { PurchaseOrderItemMapper.class })
public interface PurchaseOrderMapper {
    @Mapping(source = "items", target = "items")
    PurchaseOrderResponseDto toDto(PurchaseOrder entity);

    @Mapping(source = "items", target = "items")
    PurchaseOrder toEntity(PurchaseOrderRequestDto dto);

    List<PurchaseOrderResponseDto> toResponseList(List<PurchaseOrder> entities);

    List<PurchaseOrder> toEntityList(List<PurchaseOrderResponseDto> dtos);

}
