package com.datn.ailms.interfaces;


import com.datn.ailms.model.dto.request.ChangePasswordRequestDto;
import com.datn.ailms.model.dto.request.UserRequestDto;

import com.datn.ailms.model.dto.response.UserResponseDto;

import com.datn.ailms.model.dto.response.stats.UserStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;


public interface IUserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByEmail(String email);

    Page<UserResponseDto> searchUsers (String name,String role, Boolean gender, Boolean status
            ,Pageable pageable);

    List<UserResponseDto> getUsersByNameContainingIgnoreCase(String name);
    
    UserResponseDto getUserById(UUID userid);

    UserResponseDto createUser(UserRequestDto userRequest);

    UserResponseDto updateUser(UUID userId, UserRequestDto userRequest);

    void changePassword(String username, ChangePasswordRequestDto request);

     UserResponseDto activeAccount(UUID id);
     UserResponseDto blockedAccount(UUID id);

    UserStatsDto getUserStats();
}
