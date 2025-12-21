package com.datn.ailms.repositories;

import com.datn.ailms.model.entities.Stock;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findByProductDetailAndWarehouse(
            ProductDetail productDetail,
            Warehouse warehouse
    );

}
