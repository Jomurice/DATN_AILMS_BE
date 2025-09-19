package com.datn.ailms.model.entities.topo_entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shelves")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     UUID id;

    String name;

     String code;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "aisle_id")
     Aisle aisle;

    @OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL)
     List<Bin> bins;
}
