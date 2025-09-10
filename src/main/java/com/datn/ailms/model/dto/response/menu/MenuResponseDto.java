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
public class MenuResponseDto {
    UUID id;
    String path;
    UUID parentId;
    String title;
    List<MenuResponseDto> children;
}
