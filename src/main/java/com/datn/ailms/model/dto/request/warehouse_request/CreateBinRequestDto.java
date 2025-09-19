package com.datn.ailms.model.dto.request.warehouse_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBinRequestDto {
    String name;
    String code;
    Integer capacity;
    Integer currentQty;
    Long preferredProductId;
    UUID shelfId;
}