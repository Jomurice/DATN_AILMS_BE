package com.datn.ailms.model.dto.response.menu;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuListResponseDto {
    UUID id;
    String path;
    UUID parentId;
    String title;
    List<MenuListResponseDto> children;
}
