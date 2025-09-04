package com.datn.ailms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(1001,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    CANT_FIND_SERIAL_NUMBER(1006,"Dont have Serial Number",HttpStatus.NOT_FOUND),

    USERNAME_EXISTED(1006, "User already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1007, "Email already exists", HttpStatus.CONFLICT),


    ROLE_NOT_EXISTED(1008, "Role not existed", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1008, "Role already exists", HttpStatus.CONFLICT),

    CATEGORY_NOT_EXISTED(1010,"Category not existed", HttpStatus.NOT_FOUND),

    OTP_NOT_EXISTED(1020, "OTP not existed", HttpStatus.NOT_FOUND),
    OTP_EXPIRED(1021, "OTP expired", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1022, "OTP invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INCORRECT(1023, "Password incorrect", HttpStatus.BAD_REQUEST),
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
