package com.datn.ailms.model.dto.response.order;

import com.datn.ailms.model.entities.order_entites.Supplier;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierResponseDto {
    UUID id;
    String companyName;
    String contactName;
    String email;
    String phone;
    String address;
    boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
