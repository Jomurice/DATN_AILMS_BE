package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, UUID> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<OutboundOrder> findById(UUID outboundId);

    List<OutboundOrder> findByStatusAndCreateAtBefore(String status, LocalDateTime time);

    @EntityGraph(attributePaths = {"items"}) // tạm vì chưa có product
    @Query("SELECT o FROM OutboundOrder o WHERE o.status <> :status")
    List<OutboundOrder> findAll(String status);

    @EntityGraph(attributePaths = {"items"})
    List<OutboundOrder> findByStatus(String status);

    @EntityGraph(attributePaths = {"items"})
    @Query("SELECT DISTINCT o FROM OutboundOrder o JOIN o.items i WHERE i.product.id = :productId")
    List<OutboundOrder> findByProductId(@Param("productId") UUID productId);
}
