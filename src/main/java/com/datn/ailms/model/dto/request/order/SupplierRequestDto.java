package com.datn.ailms.model.dto.request.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierRequestDto {
        String companyName;
        String contactName;
        String email;
        String phone;
        String address;

    }

