package com.datn.ailms.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponseDto {
    String name;
    String description;
    Set<PermissionResponseDto> roles;
}
