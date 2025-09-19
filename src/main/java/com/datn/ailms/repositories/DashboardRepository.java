package com.datn.ailms.repositories;

import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DashboardRepository extends JpaRepository<ProductDetail, UUID> {
    @Query("""
    SELECT 
        FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD') as day,
        COUNT(pd.id) as total
    FROM ProductDetail pd
    WHERE pd.status = :status
      AND pd.createdAt BETWEEN :startDate AND :endDate
    GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD')
    ORDER BY day
""")
    List<Object[]> countByStatusAndDay(
            @Param("status") SerialStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Group by HOUR
    @Query("""
        SELECT 
            FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD HH24') as hour,
            COUNT(pd.id) as total
        FROM ProductDetail pd
        WHERE pd.status = :status
          AND pd.createdAt BETWEEN :startDate AND :endDate
        GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM-DD HH24')
        ORDER BY hour
    """)
    List<Object[]> countByStatusAndHour(
            @Param("status") SerialStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Group by MONTH
    @Query("""
        SELECT 
            FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM') as month,
            COUNT(pd.id) as total
        FROM ProductDetail pd
        WHERE pd.status = :status
          AND pd.createdAt BETWEEN :startDate AND :endDate
        GROUP BY FUNCTION('TO_CHAR', pd.createdAt, 'YYYY-MM')
        ORDER BY month
    """)
    List<Object[]> countByStatusAndMonth(
            @Param("status") SerialStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
