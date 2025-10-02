package com.datn.ailms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(1001,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    CANT_FIND_SERIAL_NUMBER(1006,"Dont have Serial Number",HttpStatus.NOT_FOUND),

    USERNAME_EXISTED(1006, "User already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1007, "Email already exists", HttpStatus.CONFLICT),
    PRODUCT_NOT_FOUND(1008, "Product not found", HttpStatus.NOT_FOUND),
    MENU_EXISTED(1009, "Menu already exists", HttpStatus.CONFLICT),
    PARENT_NOT_FOUND(1010, "Parent not found", HttpStatus.NOT_FOUND),
    MENU_NOT_FOUND(1011,"Menu not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1008, "Role not existed", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1008, "Role already exists", HttpStatus.CONFLICT),

    CATEGORY_NOT_EXISTED(1010,"Category not existed", HttpStatus.NOT_FOUND),

    OTP_NOT_EXISTED(1020, "OTP not existed", HttpStatus.NOT_FOUND),
    OTP_EXPIRED(1021, "OTP expired", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1022, "OTP invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INCORRECT(1023, "Password incorrect", HttpStatus.BAD_REQUEST),

    WAREHOUSE_NOT_EXISTED(1025, "Warehouse not existed", HttpStatus.NOT_FOUND),
    ZONE_NOT_EXISTED(1026, "Zone not existed", HttpStatus.NOT_FOUND),
    AISLE_NOT_EXISTED(1027, "AISLE not existed", HttpStatus.NOT_FOUND),
    SHELF_NOT_EXISTED(1028, "Shell not existed", HttpStatus.NOT_FOUND),
    BIN_NOT_EXISTED(1028, "Bin not existed", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(1029,"Order not found",HttpStatus.NOT_FOUND),
    INVALID_PARAM(1030,"Invalid Parameter", HttpStatus.BAD_REQUEST),
    SERIAL_ALREADY_SCANNED(1031,"Serial had been scanned", HttpStatus.CONFLICT),
    BIN_FULL(1032,"No more space in Bin", HttpStatus.CONFLICT),
    SERIAL_NOT_FOUND(1033, "Serial not found", HttpStatus.NOT_FOUND),
    ORDER_ALREADY_COMPLETED(1034,"Order already completed", HttpStatus.CONFLICT),
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
