package com.datn.ailms.controllers;

import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.UserResponseDto;
import com.datn.ailms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService _userService;

    @PatchMapping("/{userId}/active")
    public ApiResp<UserResponseDto> activeAccount(@PathVariable UUID userId) {
        return ApiResp.<UserResponseDto>builder()
                .result(_userService.activeAccount(userId))
                .message("Account is active")
                .build();
    }

    @PatchMapping("/{userId}/blocked")
    public ApiResp<UserResponseDto> blockAccount(@PathVariable UUID userId) {
        return ApiResp.<UserResponseDto>builder()
                .result(_userService.blockedAccount(userId))
                .message("Account is blocked")
                .build();
    }


}
