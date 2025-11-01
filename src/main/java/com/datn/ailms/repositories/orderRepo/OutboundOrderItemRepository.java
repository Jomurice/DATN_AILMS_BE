package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboundOrderItemRepository extends JpaRepository<OutboundOrderItem, UUID> {
    List<OutboundOrderItem> findByOutboundOrderId(UUID outboundOrderId);

    @EntityGraph(attributePaths = {"product"})
    List<OutboundOrderItem> findByProductId(UUID productId);
}
