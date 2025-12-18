package com.datn.ailms.model.dto.request.inventory;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductConfirmRequestDto {
    private String serialNumber;  // serial quét được
    private UUID warehouseId;     // kho nhập
    private UUID scannedByUserId; // nhân viên thực hiện scan
}
