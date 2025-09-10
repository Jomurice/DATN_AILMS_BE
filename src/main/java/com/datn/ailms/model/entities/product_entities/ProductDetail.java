package com.datn.ailms.model.entities;

import com.datn.ailms.model.entities.enums.SerialStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "product_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"serialNumber"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     String id;

    @Column(nullable = false, unique = true, length = 100)
     String serialNumber;   // mỗi sản phẩm cụ thể có 1 serial riêng, duy nhất

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private SerialStatus status;

    @ManyToOne
    @JoinColumn(name = "product_id")
     Product product;

    @ManyToOne
    @JoinColumn(name = "bin_id") // ✅ mỗi serial gán vào 1 bin cụ thể
    private Bin bin;
}

