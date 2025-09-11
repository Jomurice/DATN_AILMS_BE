package com.datn.ailms.repositories;

import com.datn.ailms.model.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    boolean existsByName(String name);
}
