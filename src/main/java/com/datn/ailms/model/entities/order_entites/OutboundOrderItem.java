package com.datn.ailms.model.entities.order_entites;

import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class OutboundOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    Integer orderQuantity;
    Integer scannedQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outboundOrder_id", nullable = true)
    OutboundOrder outboundOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @OneToMany(mappedBy = "outboundOrderItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductDetail> productDetails = new ArrayList<>();
}
