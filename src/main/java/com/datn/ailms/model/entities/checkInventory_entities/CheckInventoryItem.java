package com.datn.ailms.model.entities.checkInventory_entities;

import com.datn.ailms.model.entities.product_entities.ProductDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "check_inventory_item")
public class CheckInventoryItem {
    @Id
    @GeneratedValue
    @Column (name = "id")
    private UUID id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_inventory_id", nullable = false)
    private CheckInventory checkInventory; // reference ến phiếu

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_details_id", nullable = false)
    private ProductDetail productDetails;  // Reference đến product (id là String)

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "checked_time")
    private LocalDateTime checkedTime;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }




}
