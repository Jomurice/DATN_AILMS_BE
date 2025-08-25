package com.datn.ailms.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String username;
    String name;
    String password;
    String phone;
    String email;
    boolean gender;
    Set<String> roles;
}
