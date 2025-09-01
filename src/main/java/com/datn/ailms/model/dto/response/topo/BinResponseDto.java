package com.datn.ailms.model.dto.response.topo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinResponseDto {
    private UUID id;
    private String name;
    private String code;
    private Integer capacity;
    private Integer currentQty;
    private Long preferredProductId;
}
