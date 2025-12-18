package com.datn.ailms.interfaces;

import com.datn.ailms.model.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface IStockRepository extends JpaRepository<Stock, UUID> {
    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.productDetail.product.id = :productId")
    Optional<Integer> findQuantity(@Param("productId") UUID productId);


}
