package com.datn.ailms.model.entities;

import com.datn.ailms.model.entities.product_entities.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "brand")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    // Many-to-Many với Product
    @ManyToMany(mappedBy = "brands")
    Set<Product> products = new HashSet<>();
}