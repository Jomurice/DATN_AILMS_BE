package com.datn.ailms.controllers;

import com.datn.ailms.model.dto.request.AuthenRequest;
import com.datn.ailms.model.dto.request.IntrospectRequest;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.AuthenResponse;
import com.datn.ailms.model.dto.response.IntrospectResponse;
import com.datn.ailms.interfaces.IAuthenticationService;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {

    @Autowired
    IAuthenticationService IAuthenticationService;

    @PostMapping("/token")
    ApiResp<AuthenResponse> authenticate(@RequestBody AuthenRequest request) throws JsonEOFException, ParseException {
        var result = IAuthenticationService.authenticate(request);
        return ApiResp.<AuthenResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResp<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = IAuthenticationService.introspect(request);
        return ApiResp.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResp<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return ApiResp.<Void>builder()
                .message("Logout success")
                .build();
    }





}
