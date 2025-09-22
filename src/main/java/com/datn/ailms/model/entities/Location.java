package com.datn.ailms.model.entities;

import com.datn.ailms.model.entities.topo_entities.Warehouse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String address;

    String name;

    @ManyToOne
    @JoinColumn(name ="parent_id")
    Location parent;

    @OneToMany(mappedBy = "parent")
    List<Location> children = new ArrayList<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    List<Warehouse> warehouses = new ArrayList<>();
}
