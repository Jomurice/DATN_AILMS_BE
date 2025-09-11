package com.datn.ailms.model.entities.topo_entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;

     String code;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
     Warehouse warehouse;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
     List<Aisle> aisles;
}
