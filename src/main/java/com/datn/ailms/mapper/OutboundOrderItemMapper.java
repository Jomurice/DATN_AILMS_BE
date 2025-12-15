package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.order.OutboundOrderItemRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import com.datn.ailms.model.entities.product_entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, ProductDetailMapper.class})
public interface OutboundOrderItemMapper {

    @Mapping(source = "product", target = "product")
    @Mapping(source = "productDetails", target = "productDetails")
    OutboundOrderItemResponseDto toDto(OutboundOrderItem entity);

    @Mapping(source = "productId", target = "product.id")
    OutboundOrderItem toEntity (OutboundOrderItemRequestDto dto);

    List<OutboundOrderItemResponseDto> toDtoList(List<OutboundOrderItem> entities);

    List<OutboundOrderItem> toEntityList(List<OutboundOrderRequestDto> dto);

}
