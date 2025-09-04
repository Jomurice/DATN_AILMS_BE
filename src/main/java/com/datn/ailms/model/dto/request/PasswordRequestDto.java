package com.datn.ailms.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordRequestDto {
    String email;
    String otpCode;
    String newPassword;
    String confirmPassword;
}
