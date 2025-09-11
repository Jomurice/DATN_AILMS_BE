package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.IRoleService;
import com.datn.ailms.model.dto.request.RoleRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.RoleResponseDto;
import com.datn.ailms.model.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {


    @Autowired
    private IRoleService _roleService;

    @GetMapping
    public ApiResp<List<RoleResponseDto>> getAllRoles() {
        return ApiResp.<List<RoleResponseDto>>
                builder()
                .result(_roleService.getAllRoles())
                .build();
    }

    @GetMapping("/{roleId}")
    public ApiResp<RoleResponseDto> getRoleById(@PathVariable("roleId") String roleId) {
        return ApiResp.<RoleResponseDto>builder()
                .result(_roleService.getRoleById(roleId))
                .build();
    }

    @PostMapping
    public ApiResp<RoleResponseDto> createRole(@RequestBody RoleRequestDto roleRequestDto) {
        return ApiResp.<RoleResponseDto>builder()
                .result(_roleService.createRole(roleRequestDto))
                .build();
    }

    @PutMapping("/{roleId}")
    public ApiResp<RoleResponseDto> updateRole(@PathVariable("roleId") String roleId, @RequestBody RoleRequestDto roleRequestDto) {
        return ApiResp.<RoleResponseDto>builder()
                .result(_roleService.updateRole(roleId, roleRequestDto))
                .build();
    }

    @DeleteMapping("/{roleId}")
    public ApiResp<Void> deleteRole(@PathVariable("roleId") String roleId){
        _roleService.deleteRole(roleId);
        return ApiResp.<Void>builder()
                .message("Role deleted")
                .build();
    }
}
