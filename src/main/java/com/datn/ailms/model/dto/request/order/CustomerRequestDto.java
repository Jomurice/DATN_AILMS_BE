package com.datn.ailms.model.dto.request.order;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequestDto {
    String lastName;
    String firstName;
    String phone;
    String email;
    String address;
    boolean gender;
    LocalDate dob;

}
