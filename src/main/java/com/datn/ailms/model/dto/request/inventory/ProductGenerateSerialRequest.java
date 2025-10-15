package com.datn.ailms.model.dto.request.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGenerateSerialRequest {
    private UUID purchaseOrderItemId;
    private UUID scannedByUserId; // User thực hiện scan (thường là nhân viên kho)
    private String serialNumber;
}
