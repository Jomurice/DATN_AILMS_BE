package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.UserRequestDto;
import com.datn.ailms.model.dto.response.UserResponseDto;


import java.util.List;

public interface IUserService {

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> getUsersByUsername(String Username);

    UserResponseDto getUserById(String userid);

    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto updateUser(String userId, UserRequestDto request);

}
