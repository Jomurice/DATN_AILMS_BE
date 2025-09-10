package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.menu.CreateMenuRequestDto;
import com.datn.ailms.model.dto.request.menu.UpdateMenuRequestDto;
import com.datn.ailms.model.dto.response.menu.MenuResponseDto;
import com.datn.ailms.model.entities.other_entities.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    // Entity -> Response DTO
    @Mapping(source = "parent.id", target = "parentId")
    MenuResponseDto toMenuResponse(Menu menu);

    List<MenuResponseDto> toMenuResponseList(List<Menu> menus);

    // Create DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    Menu toEntity(CreateMenuRequestDto dto);

    // Update DTO -> update vào Entity sẵn có
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    void updateEntityFromDto(UpdateMenuRequestDto dto, @MappingTarget Menu entity);
}
