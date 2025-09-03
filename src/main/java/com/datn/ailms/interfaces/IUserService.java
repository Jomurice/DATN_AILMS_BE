package com.datn.ailms.interfaces;


import com.datn.ailms.model.dto.request.UserRequestDto;

import com.datn.ailms.model.dto.response.UserResponseDto;
import com.datn.ailms.model.entities.User;


import java.util.List;

public interface IUserService {

    List<UserResponseDto> getAllUsers();



    List<UserResponseDto> getUsersByNameContainingIgnoreCase(String name);
    UserResponseDto getUserById(String userid);

    UserResponseDto createUser(UserRequestDto userRequest);

    UserResponseDto updateUser(String userId, UserRequestDto userRequest);

}
