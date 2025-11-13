package com.datn.ailms.model.dto.response.checkInventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInventoryItemResponseDTO {
    private UUID id;
    private String productDetailsId;
    private String serialNumber;
    private String status;
    private LocalDateTime checkedTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
