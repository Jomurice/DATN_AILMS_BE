package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
    Optional<Warehouse> findByCode(String code);

    // WarehouseRepository
    List<Warehouse> findByParentId(UUID parentId);
    List<Warehouse> findByLocationAndParentIsNull(String  location);

}
