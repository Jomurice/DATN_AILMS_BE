package com.datn.ailms.model.dto.response.inventory_check;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCheckItemResponseDto {
    UUID id;
    UUID productDetailId;
    String serialNumber;
    String productSku;
    String productName;
    Integer systemQuantity;
    Integer countedQuantity;
    String status;
    String note;
    LocalDateTime checkedTime;
    // Ngày nhập kho (Lấy từ ProductDetail)
    LocalDateTime importDate;
    // ✅ THÊM MỚI: Tên người quét (để hiển thị)
    String scannedByName;
}