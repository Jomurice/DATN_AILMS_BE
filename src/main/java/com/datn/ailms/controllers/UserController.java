package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.model.dto.request.UserRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.UserResponseDto;


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
    IUserService _userService;

    @GetMapping
    public ApiResp<List<UserResponseDto>> getUsers() {
        return ApiResp.<List<UserResponseDto>>builder()
                .result(_userService.getAllUsers())
                .build();
    }
    @GetMapping("/{userId}")
    public ApiResp<UserResponseDto> getUserById(@PathVariable("userId") String userId){
        UserResponseDto user = _userService.getUserById(userId);
        return ApiResp.<UserResponseDto>builder()
                .result(user)
                .build();
    }
    @GetMapping("/search")
    public ApiResp<List<UserResponseDto>> searchUsersByName(@RequestParam("name") String name) {
        return ApiResp.<List<UserResponseDto>>builder()
                .result(_userService.getUsersByNameContainingIgnoreCase(name))
                .build();
    }

    @PostMapping
    public ApiResp<UserResponseDto> createUser(@RequestBody UserRequestDto userRequest){
        UserResponseDto createdUser = _userService.createUser(userRequest);

        return ApiResp.<UserResponseDto>builder()
                .result(createdUser)
                .build();
    }
    @PutMapping("/{userId}")
    public ApiResp<UserResponseDto> updateUser(@PathVariable("userId") String userId, @RequestBody UserRequestDto userRequest){
        UserResponseDto userResponse = _userService.updateUser(userId, userRequest);
        return ApiResp.<UserResponseDto>builder().result(userResponse).build();
    }
}
