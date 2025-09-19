package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.topo_entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ZoneRepository extends JpaRepository<Zone, UUID> {

    @Query(value = "SELECT * FROM zones z WHERE z.warehouse_id = ?1", nativeQuery = true)
    List<Zone> findAllByWarehouseIdNativeQuery(UUID warehouseId);
}
