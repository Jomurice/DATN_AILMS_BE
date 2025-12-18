package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<PurchaseOrder> findById(UUID id);

    @Query("""
        SELECT po FROM PurchaseOrder po
        WHERE 
            (:status IS NULL OR po.status = :status)
            AND (:keyword IS NULL 
                 OR LOWER(po.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
        """)
    Page<PurchaseOrder> searchPurchaseOrder(
            @Param("status") String status,
            @Param("keyword") String keyword,
            Pageable pageable);
}
