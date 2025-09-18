package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.topo_entities.Aisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AisleRepository extends JpaRepository<Aisle, UUID> {

    List<Aisle> findByZoneId(UUID zoneId);

    @Query(value = "SELECT * FROM aisles a WHERE a.zone_id = ?1",nativeQuery = true)
    List<Aisle> findAllByZoneIdNativeQuery(UUID zoneId);
}
