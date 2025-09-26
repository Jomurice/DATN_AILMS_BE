package com.datn.ailms.model.dto.response.inventory;

import com.datn.ailms.model.entities.enums.SerialStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponseDto {
    private String serialNumber;
    private UUID productId;
    private UUID purchaseOrderItemId;
    private UUID warehouseId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID scannedByUserId;
}
