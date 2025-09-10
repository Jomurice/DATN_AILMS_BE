package com.datn.ailms.model.dto.request.menu;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateMenuRequestDto {
    UUID id;
    String title;
    String path;
    UUID parentId;
}
