package com.datn.ailms.model.dto.request.warehouse_request;

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
public class WarehouseRuleRequestDto {
    private String pattern;      // regex match serial
    private Integer priority;    // default = 100
    private Boolean enabled;     // default = true
    private String level;        // BIN, SHELF, WAREHOUSE...
    private UUID warehouseId;    // warehouse được map
}
