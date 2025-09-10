package com.datn.ailms.model.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String urlImage;
    String publicId;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt;


    @ManyToOne()
    @JoinColumn(name = "product_id")
    Product product;


}
