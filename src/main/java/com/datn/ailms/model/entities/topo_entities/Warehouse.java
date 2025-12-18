package com.datn.ailms.model.entities.topo_entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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


    @Column(nullable=false, unique=true, length=32)
    private String code;      // HN01, HCM01...

    @Column(nullable=false, length=128)
    private String name;

    private String location;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
     List<Zone> zones;
}
