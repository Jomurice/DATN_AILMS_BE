package com.datn.ailms.services;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IRoleService;
import com.datn.ailms.mapper.RoleMapper;
import com.datn.ailms.model.dto.request.RoleRequestDto;
import com.datn.ailms.model.dto.response.RoleResponseDto;
import com.datn.ailms.model.entities.account_entities.Role;
import com.datn.ailms.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository _roleRepository;
    private final RoleMapper _roleMapper;

    @Override
    public List<RoleResponseDto> getAllRoles() {

        return _roleMapper.toResponseList(_roleRepository.findAll());
    }

    @Override
    public RoleResponseDto getRoleById(String roleId) {
        Role role = _roleRepository.findById(roleId).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_EXISTED)
        );
        return _roleMapper.toResponse(role);
    }


    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequest) {
        Role role = _roleMapper.toEntity(roleRequest);
        if(_roleRepository.existsByName(role.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        role = _roleRepository.save(role);
        return _roleMapper.toResponse(role);
    }

    @Override
    public RoleResponseDto updateRole(String roleId, RoleRequestDto roleRequest) {
        Role role = _roleRepository.findById(roleId).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_EXISTED)
        );

        role.setDescription(roleRequest.getDescription());
//        existRole.setPermissions();
        role = _roleRepository.save(role);
        return _roleMapper.toResponse(role);
    }

    @Override
    public void deleteRole(String roleId) {
        _roleRepository.findById(roleId).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_EXISTED)
        );
        _roleRepository.deleteById(roleId);
    }


}
