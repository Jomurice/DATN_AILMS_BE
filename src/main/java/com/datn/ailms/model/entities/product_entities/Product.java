    package com.datn.ailms.model.entities.product_entities;
    
    import com.datn.ailms.model.entities.Brand;
    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;

    import java.time.LocalDateTime;
    import java.util.HashSet;
    import java.util.Set;
    import java.util.UUID;
    
    @Entity
    @Table(
            name = "products",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"sku"})
            }
    )
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        UUID id;
    
        @Column(nullable = false, unique = true, length = 50)
         String sku;   // Mã SKU, định danh loại sản phẩm
    
        @Column(nullable = false, length = 200)
         String name;

    
        @Column(length = 1000)
         String specifications; // thông số kỹ thuật (mô tả)
    
        private String color;
    
        private String storage;

        LocalDateTime createdAt;

        LocalDateTime updatedAt;

        @Column(name = "serial_prefix")
        private String serialPrefix;



    
        // Quan hệ với ProductDetail (serial numbers)
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<ProductDetail> productDetails;

        @ManyToMany
        @JoinTable(
                name = "product_brands",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "brand_id")
        )
        Set<Brand> brands = new HashSet<>();

        // Many-to-One: Product thuộc 1 category
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        private Category category;

    }
