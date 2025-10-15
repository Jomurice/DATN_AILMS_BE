package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.order.PurchaseOrderItemRequestDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderItemResponseDto;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = { ProductMapper.class, ProductDetailMapper.class })
public interface PurchaseOrderItemMapper {
    @Mapping(source = "product", target = "product")
    @Mapping(source = "productDetails", target = "productDetails")
    PurchaseOrderItemResponseDto toDto(PurchaseOrderItem entity);

    @Mapping(source = "productId", target = "product.id")
    PurchaseOrderItem toEntity(PurchaseOrderItemRequestDto dto);

    List<PurchaseOrderItemResponseDto> toDtoList(List<PurchaseOrderItem> entities);

    List<PurchaseOrderItem> toEntityList(List<PurchaseOrderItemRequestDto> dtos);
}
