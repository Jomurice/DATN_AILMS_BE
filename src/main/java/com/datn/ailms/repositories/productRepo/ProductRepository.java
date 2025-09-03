package com.datn.ailms.repositories.productRepo;

import com.datn.ailms.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String sku);
    Optional<Product> findBySerialPrefix(String serialPrefix);
}
