package com.datn.ailms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(1001,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    CANT_FIND_SERIAL_NUMBER(1006,"Dont have Serial Number",HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
