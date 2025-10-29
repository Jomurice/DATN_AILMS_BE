package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, UUID> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<OutboundOrder> findById(UUID outboundId);
}
