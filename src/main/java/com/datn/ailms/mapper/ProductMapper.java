package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.inventory.ProductRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductResponseDto;
import com.datn.ailms.model.entities.Brand;
import com.datn.ailms.model.entities.product_entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductMapper {
        // RequestDto -> Entity
        @Mapping(target = "id", ignore = true) // id set trong service
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "category", ignore = true) // gán trong service
        @Mapping(target = "brands", ignore = true)   // gán trong service
        Product toEntity(ProductRequestDto request);

        // Entity -> ResponseDto
        @Mapping(target = "categoryId", source = "category.id")
        @Mapping(target = "categoryName", source = "category.name")
        @Mapping(target = "brandId", source = "brands", qualifiedByName = "extractFirstBrandId")
        @Mapping(target = "brandName", source = "brands", qualifiedByName = "extractFirstBrandName")
        ProductResponseDto toResponse(Product product);

        List<ProductResponseDto> toResponseList(List<Product> products);

        // Helper để lấy brandId từ Set<Brand>
        @Named("extractFirstBrandId")
        default java.util.UUID extractFirstBrandId(Set<Brand> brands) {
            return brands != null && !brands.isEmpty() ? brands.iterator().next().getId() : null;
        }

        @Named("extractFirstBrandName")
        default String extractFirstBrandName(Set<Brand> brands) {
            return brands != null && !brands.isEmpty() ? brands.iterator().next().getName() : null;
        }
}
