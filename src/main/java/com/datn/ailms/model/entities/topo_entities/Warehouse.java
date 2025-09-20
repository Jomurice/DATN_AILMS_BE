package com.datn.ailms.model.entities.topo_entities;

import com.datn.ailms.model.entities.Location;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "warehouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;


    @Column(nullable = false, unique = true, length = 32)
    String code;      // HN01, HCM01...

    @Column(nullable = false, length = 128)
    String name;

    String type; // ZONE, AISLE, SHELF, BIN

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="parent_id")
    Warehouse parent;

    @OneToMany(mappedBy = "parent")
    List<Warehouse> children;


    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;

}
