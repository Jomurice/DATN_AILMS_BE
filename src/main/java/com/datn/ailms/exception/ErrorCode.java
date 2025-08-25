package com.datn.ailms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // use
    UNAUTHENTICATED(1001,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    USERNAME_EXISTED(1006, "User already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1007, "Email already exists", HttpStatus.CONFLICT),

    ;



    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
