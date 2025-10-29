package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "Spring", uses = {OutboundOrderItemMapper.class})
public interface OutboundOrderMapper {

    @Mapping(source = "items", target = "items")
    @Mapping(source = "createdBy.id", target = "createdBy")
    OutboundOrderResponseDto toDto(OutboundOrder entity);

    @Mapping(source = "items", target = "items")
    @Mapping(source = "createdBy", target = "createdBy.id")
    OutboundOrder toEntity(OutboundOrderRequestDto dto);

    List<OutboundOrderResponseDto> toResponseList( List<OutboundOrder> entities);

    default User mapUser(UUID id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}
