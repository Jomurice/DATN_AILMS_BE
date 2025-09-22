package com.datn.ailms.model.entities.topo_entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "warehouse_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, length = 255)
    String pattern; // regex match serial

    Integer priority; // default = 100

    Boolean enabled; // default = true

    @Column(length = 20)
    String level; // BIN, SHELF, WAREHOUSE... (optional)

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse; // node mà serial được map vào
}

