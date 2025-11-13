package com.datn.ailms.repositories.checkInventoryRepo;

import com.datn.ailms.model.entities.checkInventory_entities.CheckInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckInventoryRepository extends JpaRepository<CheckInventory, UUID> {
    // Thêm: Tìm phiếu theo warehouse_id (1 kho - N phiếu)
    List<CheckInventory> findByWarehouseId(UUID warehouseId);

    // Tìm theo createdBy
    List<CheckInventory> findByCreatedBy(UUID createdBy);

    // Tìm theo status
    List<CheckInventory> findByStatus(String status);

    // Thêm method này để fix lỗi line 121
    @Query("SELECT ci FROM CheckInventory ci WHERE ci.warehouse.id IN :warehouseIds")
    List<CheckInventory> findByWarehouseIdIn(@Param("warehouseIds") List<UUID> warehouseIds);
}