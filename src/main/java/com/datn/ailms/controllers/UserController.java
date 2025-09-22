package com.datn.ailms.controllers;

import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.model.dto.request.UserRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.UserResponseDto;


import com.datn.ailms.model.dto.response.stats.UserStatsDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    @Autowired
    IUserService _userService;

//    @GetMapping
//    public ApiResp<List<UserResponseDto>> getUsers() {
//        return ApiResp.<List<UserResponseDto>>builder()
//                .result(_userService.getAllUsers())
//                .build();
//    }

    @GetMapping
    public ApiResp<Page<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Boolean gender,
            Pageable pageable) {

        Page<UserResponseDto> users = _userService.searchUsers(name, role, status, gender, pageable);

        return ApiResp.<Page<UserResponseDto>>builder()
                .result(users)
                .build();
    }
///api/users?page=0&size=10
/// /api/users?name=quang&page=0&size=10
/// /api/users?role=ADMIN&page=0&size=10
/// /api/users?status=true&page=0&size=10
/// /api/users?gender=true&page=0&size=10
/// /api/users?name=quang&role=ADMIN&status=true&gender=true&page=0&size=10
/// /api/users?name=quang&page=0&size=10&sort=name,asc
///



    @GetMapping("/{userId}")
    public ApiResp<UserResponseDto> getUserById(@PathVariable("userId") UUID userId){
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
    public ApiResp<UserResponseDto> updateUser(@PathVariable("userId") UUID userId, @RequestBody UserRequestDto userRequest){
        UserResponseDto userResponse = _userService.updateUser(userId, userRequest);
        return ApiResp.<UserResponseDto>builder().result(userResponse).build();
    }

    @GetMapping("/stats")
    public ApiResp<UserStatsDto> getUserStats() {
        return ApiResp.<UserStatsDto>builder()
                .result(_userService.getUserStats())
                .build();
    }
}
