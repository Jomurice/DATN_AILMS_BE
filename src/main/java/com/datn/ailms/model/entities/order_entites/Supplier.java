package com.datn.ailms.model.entities.order_entites;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "suppliers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(name = "company_name")
    String companyName;
    String contactName;
    String email;
    String phone;
    String address;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean active;
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    List<PurchaseOrder> purchaseOrders;
}
