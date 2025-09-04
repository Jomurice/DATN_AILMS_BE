package com.datn.ailms.interfaces;


import com.datn.ailms.model.dto.request.UserRequestDto;

import com.datn.ailms.model.dto.response.UserResponseDto;



import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByEmail(String email);

    List<UserResponseDto> getUsersByNameContainingIgnoreCase(String name);
    
    UserResponseDto getUserById(String userid);

    UserResponseDto createUser(UserRequestDto userRequest);

    UserResponseDto updateUser(String userId, UserRequestDto userRequest);

}
