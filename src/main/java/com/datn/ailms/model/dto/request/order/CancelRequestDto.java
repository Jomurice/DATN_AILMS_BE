package com.datn.ailms.model.dto.request.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelRequestDto {
    String note;
    UUID canceledBy;
}
