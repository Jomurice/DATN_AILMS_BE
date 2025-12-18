package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, UUID>, JpaSpecificationExecutor<OutboundOrder> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<OutboundOrder> findById(UUID outboundId);

    Optional<OutboundOrder> findByCode(String codeOrder);


    List<OutboundOrder> findByStatusAndCreateAtBefore(String status, LocalDateTime time);

    @EntityGraph(attributePaths = {"items"}) // tạm vì chưa có product
    @Query("SELECT o FROM OutboundOrder o WHERE o.status <> :status")
    Page<OutboundOrder> findAll(String status, Pageable pageable);

    @EntityGraph(attributePaths = {"items"})
    @Query("SELECT DISTINCT o FROM OutboundOrder o JOIN o.items i WHERE i.product.id = :productId")
    List<OutboundOrder> findByProductId(@Param("productId") UUID productId);


}
