package com.datn.ailms.model.entities.order_entites;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "customer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String lastName;
    String firstName;
    @Column(length = 11)
    String phone;

    String email;
    String address;
    LocalDate dob;
    boolean gender;
    boolean status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    List<OutboundOrder> outboundOrders;
}
