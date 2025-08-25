package com.datn.ailms.mapper;

import com.datn.ailms.model.entities.Role;
import org.mapstruct.Mapper;

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
}
