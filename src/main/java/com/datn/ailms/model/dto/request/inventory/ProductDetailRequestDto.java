package com.datn.ailms.model.dto.request.inventory;

import com.datn.ailms.model.entities.enums.SerialStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailRequestDto {
    private String serialNumber;
    private SerialStatus status;
    private UUID productId; // liên kết Product
    private UUID binId;     // liên kết Bin
}
