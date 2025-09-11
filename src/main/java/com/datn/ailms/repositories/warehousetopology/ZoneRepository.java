package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ZoneRepository extends JpaRepository<Zone, UUID> {
}
