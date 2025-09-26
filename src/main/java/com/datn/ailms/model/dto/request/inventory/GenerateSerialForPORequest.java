package com.datn.ailms.model.dto.request.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateSerialForPORequest {
    private UUID purchaseOrderId;   // PO chứa nhiều items
    private UUID createdByUserId;   // User tạo serial
}
