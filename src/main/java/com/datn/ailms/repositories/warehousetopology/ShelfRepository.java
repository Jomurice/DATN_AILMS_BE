package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.topo_entities.Aisle;
import com.datn.ailms.model.entities.topo_entities.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ShelfRepository extends JpaRepository<Shelf, UUID> {
    List<Shelf> findByAisleId(UUID aisleId);

    @Query(value = "SELECT * FROM shelves s WHERE s.aisle_id = ?1", nativeQuery = true)
    List<Shelf> findAllByAisleIdNativeQuery(UUID shelfId);
}
