package com.datn.ailms.repositories.productRepo;

import com.datn.ailms.model.dto.response.ProductDetailSerialDto;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, UUID> {
    Optional<ProductDetail> findBySerialNumber(String serialNumber);

    @Query("SELECT COUNT(p) FROM ProductDetail p ")
    long countProductDetail();

    @Query("SELECT COUNT(p) FROM ProductDetail p WHERE p.status = :status")
    long countByStatus(@Param("status") SerialStatus status);

    List<ProductDetail> findByProductId(UUID productId);

    Optional<ProductDetail> findBySerialNumberIgnoreCase(String serialNumber);

    @Query("SELECT p FROM ProductDetail p " +
            "JOIN FETCH p.purchaseOrderItem poi " +
            "JOIN FETCH p.product prod")
    List<ProductDetail> findAllWithPOAndProduct();

    boolean existsBySerialNumber(String serialNumber);
//    ProductDetail findBySerialNumber(String serialNumber);



}
