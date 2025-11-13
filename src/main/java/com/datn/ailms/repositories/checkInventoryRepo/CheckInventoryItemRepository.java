package com.datn.ailms.repositories.checkInventoryRepo;

import com.datn.ailms.model.entities.checkInventory_entities.CheckInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckInventoryItemRepository extends JpaRepository<CheckInventoryItem, UUID> {
    @Query("SELECT i FROM CheckInventoryItem i WHERE i.serialNumber = :serial AND i.checkInventory.id = :checkId")
    Optional<CheckInventoryItem> findBySerialNumberAndCheckInventoryId(@Param("serial") String serial, @Param("checkId") UUID checkId);
}