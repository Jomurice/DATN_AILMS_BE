package com.datn.ailms.repositories.inventoryRepo;

import com.datn.ailms.model.entities.inventory_entities.InventoryCheck;
import com.datn.ailms.model.entities.inventory_entities.InventoryCheckItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryCheckRepository extends JpaRepository<InventoryCheck, UUID> {
    @EntityGraph(attributePaths = {"warehouse", "createdBy", "checkedBy"})
    List<InventoryCheck> findAll();

    @EntityGraph(attributePaths = {"items", "warehouse", "createdBy", "checkedBy"})
    Optional<InventoryCheck> findById(UUID id);

    long count();
}