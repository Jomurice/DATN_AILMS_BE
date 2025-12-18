package com.datn.ailms.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
@Component
public class OtpGenerator {

    public String generateOtp(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            builder.append(chars.charAt(index));
        }
        return builder.toString();
    }

    public String generateCodeNumbers(int length) {

        if(length <= 0){
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String digitalCode = String.valueOf(random.nextInt(10));
            builder.append(digitalCode);
        }
        return builder.toString();
    }
}
