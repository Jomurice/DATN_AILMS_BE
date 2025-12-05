package com.datn.ailms.model.entities.inventory_entities;

import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
//@Data
@Getter // ✅ SỬA
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "check_inventory_item")
public class InventoryCheckItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_inventory_id", nullable = false)
    InventoryCheck inventoryCheck;

    // LƯU Ý: product_detail_id (UUID) phải khớp với ProductDetail entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = true)
    ProductDetail productDetail;

    @Column(length = 100)
    String serialNumber;

    Integer systemQuantity = 0;
    Integer countedQuantity = 0;

    @Column(length = 32)
    String status; // MATCHED, SHORTAGE, OVERAGE, UNKNOWN

    String note;

    // ✅ THÊM MỚI: Lưu người quét
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scanned_by")
    User scannedBy;

    LocalDateTime checkedTime;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}