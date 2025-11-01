package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, UUID> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<OutboundOrder> findById(UUID outboundId);

    @EntityGraph(attributePaths = {"items"}) // tạm vì chưa có product
    List<OutboundOrder> findAll();

    @EntityGraph(attributePaths = {"items"})
    List<OutboundOrder> findByStatus(String status);

    @EntityGraph(attributePaths = {"items"})
    @Query("SELECT DISTINCT o FROM OutboundOrder o JOIN o.items i WHERE i.product.id = :productId")
    List<OutboundOrder> findByProductId(@Param("productId") UUID productId);
}
