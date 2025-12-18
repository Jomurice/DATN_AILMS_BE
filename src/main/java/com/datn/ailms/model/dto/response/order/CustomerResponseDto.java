package com.datn.ailms.model.dto.response.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponseDto {
    UUID id;
    String lastName;
    String firstName;
    String phone;
    String email;
    String address;
    boolean gender;
    boolean status;
    LocalDate dob;
    LocalDate createAt;
    LocalDate updateAt;

}
