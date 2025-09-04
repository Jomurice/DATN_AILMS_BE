package com.datn.ailms.controllers;

import com.datn.ailms.model.dto.request.EmailRequestDto;
import com.datn.ailms.model.dto.request.PasswordRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.services.PasswordResetService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final PasswordResetService _passwordResetService;

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
}
