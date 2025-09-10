package com.datn.ailms.repositories.categoryBrand;

import com.datn.ailms.model.entities.CategoryBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryBrandRepository extends JpaRepository<CategoryBrand, UUID> {
    Optional<CategoryBrand> findByCategoryIdAndBrandId(UUID categoryId, UUID brandId);
}
