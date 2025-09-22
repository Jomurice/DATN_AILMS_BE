package com.datn.ailms.repositories;

import com.datn.ailms.model.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    List<Location> findByParentId(UUID parentId);
    List<Location> findByParentIsNull();
}
