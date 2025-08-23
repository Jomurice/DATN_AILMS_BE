package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.response.UserResponse;

import java.util.List;

public interface IUserService {

    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);
}
