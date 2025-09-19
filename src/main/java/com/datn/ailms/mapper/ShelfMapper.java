package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateShelfRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.ShelfResponseDto;
import com.datn.ailms.model.entities.topo_entities.Shelf;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShelfMapper {
    Shelf toShelf(CreateShelfRequestDto request);

    @Mapping(source = "aisle.id", target = "aisleId")
    ShelfResponseDto toShelfResponseDto(Shelf shelf);
    List<ShelfResponseDto> toShelfResponseDtoList(List<Shelf> shelves);
}