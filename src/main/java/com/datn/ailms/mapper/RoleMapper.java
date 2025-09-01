package com.datn.ailms.mapper;

import com.datn.ailms.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    default String toString(Role role){
        return role.getName();
    }
}
