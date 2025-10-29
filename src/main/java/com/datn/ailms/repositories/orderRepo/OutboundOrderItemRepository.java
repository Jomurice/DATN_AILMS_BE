package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboundOrderItemRepository extends JpaRepository<OutboundOrderItem, UUID> {
    List<OutboundOrderItem> findByOutboundOrderId(UUID outboundOrderId);
}
