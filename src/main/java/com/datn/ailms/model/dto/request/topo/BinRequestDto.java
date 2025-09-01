package com.datn.ailms.model.dto.request.topo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinRequestDto {
    private String name;
    private String code;
    private Integer capacity;
    private Integer currentQty;
    private Long preferredProductId;
    private UUID shelfId; // thay vì Shelf object
}
