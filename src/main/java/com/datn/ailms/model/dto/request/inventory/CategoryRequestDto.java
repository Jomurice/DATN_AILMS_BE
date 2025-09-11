package com.datn.ailms.model.dto.request.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Request
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {
    private String name;
    private String description;
    private UUID menuId;
}



