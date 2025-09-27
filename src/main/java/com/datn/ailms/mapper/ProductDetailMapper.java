package com.datn.ailms.mapper;


import com.datn.ailms.model.dto.request.inventory.ProductDetailRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "warehouseId", target = "warehouse.id")
    @Mapping(source = "scannedBy", target = "scannedBy.id")
    ProductDetail toEntity(ProductDetailRequestDto request);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(target = "purchaseOrderItemId", source = "purchaseOrderItem.id")
    @Mapping(source = "scannedBy.id", target = "scannedByUserId")
    ProductDetailResponseDto toResponse(ProductDetail productDetail);

    List<ProductDetailResponseDto> toResponseList(List<ProductDetail> details);

}
