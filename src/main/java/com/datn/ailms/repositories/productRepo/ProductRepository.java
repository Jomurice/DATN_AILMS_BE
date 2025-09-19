package com.datn.ailms.repositories.productRepo;

import com.datn.ailms.model.entities.product_entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String sku);
    Optional<Product> findBySerialPrefix(String serialPrefix);
    @Query("SELECT p FROM Product p WHERE p.categoryBrand.category.menu.id = :menuId")
    List<Product> findByMenuId(@Param("menuId") UUID menuId);

    @Query("""
    SELECT p FROM Product p
    WHERE UPPER(:serial) LIKE UPPER(p.serialPrefix) || '%'
""")
    Optional<Product> findBySerialMatch(@Param("serial") String serial);



}
