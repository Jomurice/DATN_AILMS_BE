package com.datn.ailms.model.dto.response.inventory;

import com.datn.ailms.model.dto.response.BinRuleResponseDto;
import com.datn.ailms.model.entities.SerialStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponseDto {
    private String id;
    private String serialNumber;
    private SerialStatus status;
    private UUID productId;
    private UUID binId;
}
