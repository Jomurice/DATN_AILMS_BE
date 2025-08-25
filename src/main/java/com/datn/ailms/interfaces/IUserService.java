package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.UserRequest;
import com.datn.ailms.model.dto.response.UserResponse;


import java.util.List;

public interface IUserService {

    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);

    UserResponse createUser(UserRequest userRequest);


}
