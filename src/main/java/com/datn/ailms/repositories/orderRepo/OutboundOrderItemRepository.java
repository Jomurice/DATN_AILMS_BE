package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OutboundOrderItemRepository extends JpaRepository<OutboundOrderItem, UUID> {
    List<OutboundOrderItem> findByOutboundOrderId(UUID outboundOrderId);

    @EntityGraph(attributePaths = {"product"})
    List<OutboundOrderItem> findByProductId(UUID productId);

//    @Query("""
//        SELECT COALESCE(SUM(oi.quantity),0)
//        FROM OutboundOrderItem oi
//        WHERE oi.createdAt >= :fromDate
//        AND oi.status
//        """)
//    long sumExportedQty(LocalDateTime fromDate, UUID warehouseId);

    Optional<OutboundOrderItem> findByOutboundOrderIdAndProductId(UUID outboundOrderId, UUID productId);
}
