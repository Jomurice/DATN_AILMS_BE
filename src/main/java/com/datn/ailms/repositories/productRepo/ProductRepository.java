package com.datn.ailms.repositories.productRepo;

import com.datn.ailms.model.entities.product_entities.Category;
import com.datn.ailms.model.entities.product_entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String sku);

    Optional<Product> findBySerialPrefix(String serialPrefix);

    @Query("SELECT p FROM Product p WHERE p.category.menu.id = :menuId")
    List<Product> findByMenuId(@Param("menuId") UUID menuId);

    @Query("""
                SELECT p FROM Product p
                WHERE UPPER(:serial) LIKE UPPER(p.serialPrefix) || '%'
            """)
    Optional<Product> findBySerialMatch(@Param("serial") String serial);

    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByCategoryAndNameContainingIgnoreCase(Category category, String name, Pageable pageable);

    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN p.brands b
    WHERE (:name IS NULL OR p.name ILIKE :name)
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:brandId IS NULL OR b.id = :brandId)
""")
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("categoryId") UUID categoryId,
            @Param("brandId") UUID brandId,
            Pageable pageable
    );



}
