package com.datn.ailms.model.entities.checkInventory_entities;

import com.datn.ailms.model.entities.account_entities.User;  // Nếu cần User cho createdBy/checkedBy
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.topo_entities.Warehouse;  // Import Warehouse
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "check_inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInventory {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status", length = 20)
    private String status;  // "UNCHECKED" hoặc "CHECKED"

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "checked_by")
    private UUID checkedBy;

    // Thêm: Quan hệ N:1 với Warehouse (nhiều phiếu kiểm thuộc 1 kho)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // Many-to-Many: Một phiếu check nhiều product_details (serial trong kho)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "check_inventory_product_details",
            joinColumns = @JoinColumn(name = "check_inventory_id"),
            inverseJoinColumns = @JoinColumn(name = "product_details_id")
    )
    private Set<ProductDetail> productDetails = new HashSet<>();

    // Một phiếu có nhiều item
    @OneToMany(mappedBy = "checkInventory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CheckInventoryItem> items = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addItem(CheckInventoryItem item) {
        items.add(item);
        item.setCheckInventory(this);
    }

    public void removeItem(CheckInventoryItem item) {
        items.remove(item);
        item.setCheckInventory(null);
    }
}