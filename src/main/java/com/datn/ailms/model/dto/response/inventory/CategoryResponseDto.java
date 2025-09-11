package com.datn.ailms.model.dto.response.inventory;

import com.datn.ailms.model.dto.response.menu.MenuResponseDto;
import com.datn.ailms.model.entities.other_entities.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Response
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private String description;
    private MenuResponseDto menu;
}
