package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    @Query(value = "SELECT * FROM suppliers s " +
            "WHERE (:active IS NULL OR s.active = :active) " +
            "AND unaccent(lower(s.company_name)) LIKE unaccent(lower(concat('%', :companyName, '%')))",
            nativeQuery = true)
    Page<Supplier> searchSuppliers(@Param("companyName") String companyName,
                                   @Param("active") Boolean active,
                                   Pageable pageable);
}
