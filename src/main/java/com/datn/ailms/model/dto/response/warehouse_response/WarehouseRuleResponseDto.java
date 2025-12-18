package com.datn.ailms.model.dto.response.warehouse_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseRuleResponseDto {
    private UUID id;
    private String pattern;
    private Integer priority;
    private Boolean enabled;
    private String level;

    private UUID warehouseId;
    private String warehouseCode;
    private String warehouseName;
}
