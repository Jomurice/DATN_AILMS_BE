package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.topo_entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
    Optional<Warehouse> findByCode(String code);
}
