package com.datn.ailms.repositories;

import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DashboardRepository extends JpaRepository<ProductDetail, UUID> {
    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD') AS day,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.IN_WAREHOUSE
      AND pd.createdAt BETWEEN :startDate AND :endDate
    GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD')
    ORDER BY day
""")
    List<Object[]> countInboundByDay(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD HH24') AS hour,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.IN_WAREHOUSE
      AND pd.createdAt BETWEEN :startDate AND :endDate
    GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD HH24')
    ORDER BY hour
""")
    List<Object[]> countInboundByHour(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM') AS month,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.IN_WAREHOUSE
      AND pd.createdAt BETWEEN :startDate AND :endDate
    GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM')
    ORDER BY month
""")
    List<Object[]> countInboundByMonth(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD') AS day,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.IN_WAREHOUSE
      AND pd.createdAt BETWEEN :start AND :end
      AND pd.warehouse.id = :warehouseId
    GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD')
    ORDER BY day
""")
    List<Object[]> countInboundByDayAndWarehouse(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("warehouseId") UUID warehouseId
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD HH24') AS hour,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.IN_WAREHOUSE
      AND pd.createdAt BETWEEN :start AND :end
      AND pd.warehouse.id = :warehouseId
    GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD HH24')
    ORDER BY hour
""")
    List<Object[]> countInboundByHourAndWarehouse(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("warehouseId") UUID warehouseId
    );

    @Query("""
    SELECT
        FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM') AS month,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.IN_WAREHOUSE
      AND pd.createdAt BETWEEN :start AND :end
      AND pd.warehouse.id = :warehouseId
    GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM')
    ORDER BY month
""")
    List<Object[]> countInboundByMonthAndWarehouse(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("warehouseId") UUID warehouseId
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD') AS day,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.OUTBOUND
      AND pd.updatedAt BETWEEN :startDate AND :endDate
    GROUP BY FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD')
    ORDER BY day
""")
    List<Object[]> countOutboundByDay(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD HH24') AS hour,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.OUTBOUND
      AND pd.updatedAt BETWEEN :startDate AND :endDate
    GROUP BY FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD HH24')
    ORDER BY hour
""")
    List<Object[]> countOutboundByHour(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM') AS month,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.OUTBOUND
      AND pd.updatedAt BETWEEN :startDate AND :endDate
    GROUP BY FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM')
    ORDER BY month
""")
    List<Object[]> countOutboundByMonth(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD') AS day,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.OUTBOUND
      AND pd.updatedAt BETWEEN :start AND :end
      AND pd.warehouse.id = :warehouseId
    GROUP BY FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD')
    ORDER BY day
""")
    List<Object[]> countOutboundByDayAndWarehouse(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("warehouseId") UUID warehouseId
    );

    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD HH24') AS hour,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.OUTBOUND
      AND pd.updatedAt BETWEEN :start AND :end
      AND pd.warehouse.id = :warehouseId
    GROUP BY FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM-DD HH24')
    ORDER BY hour
""")
    List<Object[]> countOutboundByHourAndWarehouse(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("warehouseId") UUID warehouseId
    );

    @Query("""
    SELECT
        FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM') AS month,
        COUNT(pd.id) AS total
    FROM ProductDetail pd
    WHERE pd.status = com.datn.ailms.model.entities.enums.SerialStatus.OUTBOUND
      AND pd.updatedAt BETWEEN :start AND :end
      AND pd.warehouse.id = :warehouseId
    GROUP BY FUNCTION('TO_CHAR', pd.updatedAt, 'YYYY-MM')
    ORDER BY month
""")
    List<Object[]> countOutboundByMonthAndWarehouse(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("warehouseId") UUID warehouseId
    );


}
