package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.RoleRequestDto;
import com.datn.ailms.model.dto.response.RoleResponseDto;

import java.util.List;

public interface IRoleService {
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto getRoleById(String roleId);
    RoleResponseDto createRole(RoleRequestDto roleRequest);
    RoleResponseDto updateRole(String roleId, RoleRequestDto roleRequest);
    void deleteRole(String roleId);

}
