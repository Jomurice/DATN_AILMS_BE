package com.datn.ailms.model.dto.response.warehouse_response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BinResponseDto {
    UUID id;
    String name;
    String code;
    Integer capacity;
    Integer currentQty;
    Long preferredProductId;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    UUID shelfId;

    List<Long> productDetailIds;

}
