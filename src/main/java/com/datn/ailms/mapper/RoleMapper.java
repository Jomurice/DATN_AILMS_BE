package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.RoleRequestDto;
import com.datn.ailms.model.dto.response.RoleResponseDto;
import com.datn.ailms.model.entities.account_entities.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    default String toString(Role role){
        return role.getName();
    }

       default Role toRole(String roleName){
        Role role = new Role();
        role.setName(roleName);
        return role;
        }

        Role toEntity(RoleRequestDto roleRequest);

        RoleResponseDto toResponse(Role role);

        List<RoleResponseDto> toResponseList(List<Role> roles);


}
