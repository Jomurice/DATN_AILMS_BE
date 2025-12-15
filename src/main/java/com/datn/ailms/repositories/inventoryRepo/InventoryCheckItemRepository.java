package com.datn.ailms.repositories.inventoryRepo;

import com.datn.ailms.model.entities.inventory_entities.InventoryCheckItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryCheckItemRepository extends JpaRepository<InventoryCheckItem, UUID> {
    List<InventoryCheckItem> findByInventoryCheckId(UUID checkId);

    // Tìm kiếm Item theo Serial trong phiếu kiểm kê
    Optional<InventoryCheckItem> findByInventoryCheckIdAndSerialNumber(UUID checkId, String serialNumber);
}