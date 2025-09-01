package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.UserRequest;
import com.datn.ailms.model.dto.response.UserResponse;
import com.datn.ailms.model.entities.User;


import java.util.List;

public interface IUserService {

    List<UserResponseDto> getAllUsers();



    List<UserResponseDto> getUserByNameContainingIgnoreCase(String name);
    UserResponseDto getUserById(String userid);

    UserResponseDto createUser(UserRequestDto userRequest);

    UserResponseDto updateUser(String userId, UserRequestDto userRequest);

}
