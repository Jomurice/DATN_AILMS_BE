package com.datn.ailms.model.entities.topo_entities;

import com.datn.ailms.model.entities.product_entities.ProductDetail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bins")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;

    String code;  // ví dụ: BIN-A1-S1
    @Column(nullable=false) Integer capacity;
    @Column(nullable=false) Integer currentQty;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    // gợi ý bind sản phẩm/sku cho bin này (tuỳ chọn)
    Long preferredProductId;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @OneToMany(mappedBy = "bin", cascade = CascadeType.ALL)
    private List<ProductDetail> productDetails;
}
