package com.datn.ailms.model.entities.inventory_entities;

import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
//@Data
@Getter // ✅ SỬA: Dùng Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "check_inventory")
public class InventoryCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(length = 64) // Mã phiếu
    String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Column(length = 32, nullable = false)
    String status; // DRAFT | IN_PROGRESS | COMPLETED | CANCELLED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by")
    User checkedBy;

    LocalDateTime deadline; // Ngày đến hạn kiểm kê
    String note;

    @OneToMany(mappedBy = "inventoryCheck", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<InventoryCheckItem> items;
}