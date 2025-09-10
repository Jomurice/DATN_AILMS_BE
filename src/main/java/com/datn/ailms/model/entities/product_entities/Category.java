package com.datn.ailms.model.entities.product_entities;

import com.datn.ailms.model.entities.CategoryBrand;
import com.datn.ailms.model.entities.other_entities.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    private String description;

    // Liên kết tới Menu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    Menu menu;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryBrand> categoryBrands = new HashSet<>();

//    @OneToMany(mappedBy = "category")
//    private Set<Product> products;
}
