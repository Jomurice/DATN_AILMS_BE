package com.datn.ailms.model.dto.request;

import com.datn.ailms.model.entities.enums.RuleLevel;
import lombok.Data;

import java.util.UUID;

@Data
public class BinRuleRequestDto {
    private String pattern;
    private RuleLevel level;
    private UUID binId;
    private Integer priority;
    private Boolean enabled;
}
