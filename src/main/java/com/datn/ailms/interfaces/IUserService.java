package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.UserRequest;
import com.datn.ailms.model.dto.response.UserResponse;
import com.datn.ailms.model.entities.User;


import java.util.List;

public interface IUserService {

    List<UserResponse> getAllUsers();

    List<UserResponse> getUsersByUsername(String Username);

    UserResponse getUserById(String userid);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(String userId,UserRequest userRequest);

}
