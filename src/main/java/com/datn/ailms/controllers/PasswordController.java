package com.datn.ailms.controllers;

import com.datn.ailms.model.dto.request.ChangePasswordRequestDto;
import com.datn.ailms.model.dto.request.EmailRequestDto;
import com.datn.ailms.model.dto.request.PasswordRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.entities.User;
import com.datn.ailms.services.PasswordResetService;
import com.datn.ailms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService _passwordResetService;
    private final UserService _userService;
    @PostMapping("/forgot-password")
    public ApiResp<Void> sendResetCode(@RequestBody EmailRequestDto request) {
        _passwordResetService.sendResetCode(request);
        return ApiResp.<Void>builder()
                .message("Email sent successfully")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResp<Void> resetPassword(@RequestBody PasswordRequestDto request) {
        _passwordResetService.ResetPassword(request);
        return ApiResp.<Void>builder()
                .message("Password reset successfully")
                .build();
    }

    @PostMapping("/change-password")
    public ApiResp<Void> changePassword(@RequestBody ChangePasswordRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        _userService.changePassword(username, request);

        return ApiResp.<Void>builder()
                .message("Password changed successfully")
                .build();
    }
}
