package com.datn.ailms.model.entities.topo_entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "aisles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Aisle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String code;

    String name;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(mappedBy = "aisle", cascade = CascadeType.ALL)
    private List<Shelf> shelves;
}
