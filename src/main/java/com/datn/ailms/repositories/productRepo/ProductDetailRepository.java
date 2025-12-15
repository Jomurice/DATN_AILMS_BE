package com.datn.ailms.repositories.productRepo;

import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, UUID> {
    @Query("SELECT p FROM ProductDetail p " +
            "LEFT JOIN FETCH p.product " +
            "LEFT JOIN FETCH p.warehouse " +
            "LEFT JOIN FETCH p.purchaseOrderItem " +
            "LEFT JOIN FETCH p.scannedBy " +
            "WHERE p.serialNumber = :serialNumber")
    Optional<ProductDetail> findBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query("SELECT COUNT(p) FROM ProductDetail p ")
    long countProductDetail();

    @Query("SELECT COUNT(p) FROM ProductDetail p WHERE p.status = :status")
    long countByStatus(@Param("status") SerialStatus status);

    List<ProductDetail> findByProductId(UUID productId);

    List<ProductDetail> findByWarehouseId(UUID warehouseId);
    @Query("SELECT pd FROM ProductDetail pd WHERE pd.warehouse.id = :warehouseId AND pd.status IN :statuses")
    List<ProductDetail> findByWarehouseIdAndStatusIn(@Param("warehouseId") UUID warehouseId, @Param("statuses") Collection<SerialStatus> statuses);
    List<ProductDetail> findByWarehouseIdAndStatus(UUID warehouseId, String status);
    List<ProductDetail> findByWarehouseIdAndStatus(UUID warehouseId, SerialStatus status);
    Optional<ProductDetail> findBySerialNumberIgnoreCase(String serialNumber);

    @Query("SELECT p FROM ProductDetail p " +
            "JOIN FETCH p.purchaseOrderItem poi " +
            "JOIN FETCH p.product prod")
    List<ProductDetail> findAllWithPOAndProduct();

    boolean existsBySerialNumber(String serialNumber);
//    ProductDetail findBySerialNumber(String serialNumber);

    @Query("SELECT COUNT(p) FROM ProductDetail p WHERE p.purchaseOrderItem.id = :itemId AND p.status = 'IN_WAREHOUSE'")
    int countScannedByPurchaseOrderItem(@Param("itemId") UUID itemId);

    @Query("""
    SELECT pd FROM ProductDetail pd
    JOIN pd.product p
    JOIN pd.outboundOrderItem oi
    WHERE p.sku = :sku
      AND oi.outboundOrder.id = :orderId
""")
    List<ProductDetail> findByOrderIdAndSku( UUID orderId, @Param("sku") String sku);

    @Query("""
    SELECT COUNT(pd)
    FROM ProductDetail pd
    WHERE pd.product.id = :productId
      AND pd.status = 'IN_WAREHOUSE'
""")
    long countAvailableByProductId(@Param("productId") UUID productId);

    @Query("""
    SELECT pd FROM ProductDetail pd
    WHERE pd.outboundOrderItem.outboundOrder.id = :orderId
""")
    List<ProductDetail> findByOutboundOrderId(@Param("orderId") UUID orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ProductDetail> findByProductIdAndStatus(UUID productId, SerialStatus status);

    List<ProductDetail> findByOutboundOrderItemId(UUID id);

    // --- Có lọc warehouse ---

    @Query("""
        SELECT COUNT(pd)
        FROM ProductDetail pd
        WHERE pd.status = :status
          AND pd.warehouse.id = :warehouseId
    """)
    long countByStatusAndWarehouseId(
            @Param("status") SerialStatus status,
            @Param("warehouseId") UUID warehouseId
    );
}
