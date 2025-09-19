package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<PurchaseOrder> findById(UUID id);
}
