package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.model.dto.request.UserRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.UserResponseDto;


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
    public ApiResp<List<UserResponseDto>> getUsers() {
        return ApiResp.<List<UserResponseDto>>builder()
                .result(IuserService.getAllUsers())
                .build();
    }
    @GetMapping("/{userId}")
    public ApiResp<UserResponseDto> getUserById(@PathVariable("userId") String userId){
        UserResponseDto user = IuserService.getUserById(userId);
        return ApiResp.<UserResponseDto>builder()
                .result(user)
                .build();
    }

    @PostMapping
    public ApiResp<UserResponseDto> createUser(@RequestBody UserRequestDto userRequest){
        UserResponseDto createdUser = IuserService.createUser(userRequest);

        return ApiResp.<UserResponseDto>builder()
                .result(createdUser)
                .build();
    }
    @PutMapping("/{userId}")
    public ApiResp<UserResponseDto> updateUser(@PathVariable("userId") String userId, @RequestBody UserRequestDto userRequest){
        UserResponseDto userResponse = IuserService.updateUser(userId, userRequest);
        return ApiResp.<UserResponseDto>builder().result(userResponse).build();
    }
}
