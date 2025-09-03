package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.model.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "serialPrefix", target = "serialPrefix")
    Product toEntity(ProductRequestDto request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "serialPrefix", target = "serialPrefix")
    ProductResponseDto toResponse(Product product);

    List<ProductResponseDto> toResponseList(List<Product> products);
}
