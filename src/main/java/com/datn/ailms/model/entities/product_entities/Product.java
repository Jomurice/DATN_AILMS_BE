    package com.datn.ailms.model.entities.product_entities;
    
    import com.datn.ailms.model.entities.CategoryBrand;
    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;
    
    import java.util.Set;
    import java.util.UUID;
    
    @Entity
    @Table(
            name = "products",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"sku"})
            }
    )
    @Data
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
    
         String brand;
    
        @Column(length = 1000)
         String specifications; // thông số kỹ thuật (mô tả)
    
        private String color;
    
        private String storage;

        @Column(name = "serial_prefix")
        private String serialPrefix;

        @ManyToOne
        @JoinColumn(name = "category_brand_id", nullable = false)
        private CategoryBrand categoryBrand;
    
        // Quan hệ với ProductDetail (serial numbers)
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<ProductDetail> productDetails;

    }
