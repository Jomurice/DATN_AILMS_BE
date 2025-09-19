package com.datn.ailms.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InOutSeriesDto {
    private List<String> labels;
    private List<Long> inbound;
    private List<Long> outbound;
}
