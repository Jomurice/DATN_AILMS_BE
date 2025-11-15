package com.datn.ailms.model.dto.request.inventory_check;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCheckItemRequestDto {
    UUID productDetailId;   // ID của Serial (nếu nó tồn tại trong hệ thống)
    String serialNumber;    // Bắt buộc, ngay cả khi Serial không tồn tại trong DB
    String note;
    Integer countedQuantity;
}