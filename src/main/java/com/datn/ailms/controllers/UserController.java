package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.model.dto.request.UserRequest;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.UserResponse;


import com.datn.ailms.model.entities.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    @Autowired
    IUserService IuserService;

    @GetMapping
    public ApiResp<List<UserResponse>> getUsers() {
        return ApiResp.<List<UserResponse>>builder()
                .result(IuserService.getAllUsers())
                .build();
    }
    @GetMapping("/{userId}")
    public ApiResp<UserResponse> getUserById(@PathVariable("userId") String userId){
        UserResponse user = IuserService.getUserById(userId);
        return ApiResp.<UserResponse>builder()
                .result(user)
                .build();
    }

    @PostMapping
    public ApiResp<UserResponse> createUser(@RequestBody UserRequest userRequest){
        UserResponse createdUser = IuserService.createUser(userRequest);

        return ApiResp.<UserResponse>builder()
                .result(createdUser)
                .build();
    }
    @PutMapping("/{userId}")
    public ApiResp<UserResponse> updateUser(@PathVariable("userId") String userId, @RequestBody UserRequest userRequest){
        UserResponse userResponse = IuserService.updateUser(userId, userRequest);
        return ApiResp.<UserResponse>builder().result(userResponse).build();
    }
}
