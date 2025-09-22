package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateLocationRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.LocationResponseDto;
import com.datn.ailms.model.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toEntity(CreateLocationRequestDto dto);

    @Mapping(source = "parent.id", target = "parentId")
    LocationResponseDto toResponse(Location entity);

    List<LocationResponseDto> toResponseList(List<Location> entities);
}
