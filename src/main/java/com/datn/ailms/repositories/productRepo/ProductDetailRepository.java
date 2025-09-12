package com.datn.ailms.repositories.productRepo;

import com.datn.ailms.model.entities.product_entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, UUID> {
    Optional<ProductDetail> findBySerialNumber(String serialNumber);
}
