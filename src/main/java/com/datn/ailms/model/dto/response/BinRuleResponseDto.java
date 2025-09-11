package com.datn.ailms.model.dto.response;

import com.datn.ailms.model.entities.enums.RuleLevel;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BinRuleResponseDto {
    private UUID id;
    private String pattern;
    private RuleLevel level;
    private UUID binId;
    private Integer priority;
    private Boolean enabled;
}
