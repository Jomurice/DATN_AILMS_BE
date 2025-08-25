package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.Bin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BinRepository extends JpaRepository<Bin, UUID> {
    @Query("""
     select b from Bin b
     where b.capacity > b.currentQty
       and (b.preferredProductId is null or b.preferredProductId = :productId)
     order by (b.capacity - b.currentQty) desc
    """)
    List<Bin> findAvailableForProduct(UUID productId);
}
