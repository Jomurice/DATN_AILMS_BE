package com.datn.ailms.model.dto.response.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateSerialForPOResponse {
    private UUID purchaseOrderId;
    private List<POItemSerialsResponse> itemsSerials;
}
