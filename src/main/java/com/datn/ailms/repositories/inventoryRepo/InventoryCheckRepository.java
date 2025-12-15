package com.datn.ailms.repositories.inventoryRepo;

import com.datn.ailms.model.entities.inventory_entities.InventoryCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryCheckRepository extends JpaRepository<InventoryCheck, UUID> {

    // ✅ 1. Lấy tất cả CÓ PHÂN TRANG (Thay vì List)
    @EntityGraph(attributePaths = {"warehouse", "createdBy", "checkedBy"})
    Page<InventoryCheck> findAll(Pageable pageable);

    // ✅ 2. Lọc theo trạng thái CÓ PHÂN TRANG (Dùng cho bộ lọc FE)
    @EntityGraph(attributePaths = {"warehouse", "createdBy", "checkedBy"})
    Page<InventoryCheck> findByStatus(String status, Pageable pageable);

    // 3. Lấy chi tiết (Giữ nguyên)
    @EntityGraph(attributePaths = {"items", "warehouse", "createdBy", "checkedBy"})
    Optional<InventoryCheck> findById(UUID id);

    long count();
}