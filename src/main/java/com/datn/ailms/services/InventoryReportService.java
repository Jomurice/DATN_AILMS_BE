package com.datn.ailms.services;

import com.datn.ailms.model.dto.response.InventorySummaryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<InventorySummaryDto> getInventorySummary(
            LocalDate startDate,
            LocalDate endDate,
            UUID warehouseId,
            UUID productId,
            int page,
            int size
    ) {

        if (warehouseId == null) {
            throw new IllegalArgumentException("warehouseId is required");
        }

        if (startDate == null) startDate = LocalDate.of(2000, 1, 1);
        if (endDate == null) endDate = LocalDate.now();

        String sql = """
        WITH last_check AS (
            SELECT id, updated_at
            FROM check_inventory
            WHERE warehouse_id = :warehouseId
              AND status = 'CLOSED'
            ORDER BY updated_at DESC
            LIMIT 1
        ),

        opening_from_check AS (
            SELECT
                pd.product_id,
                COUNT(*) AS opening_qty
            FROM check_inventory_item cii
            JOIN product_details pd ON pd.id = cii.product_detail_id
            JOIN last_check lc ON lc.id = cii.check_inventory_id
            WHERE cii.counted_quantity = 1
            GROUP BY pd.product_id
        ),

        opening_from_serial AS (
            SELECT
                pd.product_id,
                COUNT(*) AS opening_qty
            FROM product_details pd
            WHERE pd.warehouse_id = :warehouseId
              AND pd.status IN ('IN_WAREHOUSE', 'AVAILABLE', 'IN_STOCK')
              AND pd.created_at < :startDate
            GROUP BY pd.product_id
        ),

        opening AS (
            SELECT
                p.id AS product_id,
                COALESCE(ofc.opening_qty, ofs.opening_qty, 0) AS opening_qty
            FROM products p
            LEFT JOIN opening_from_check ofc ON ofc.product_id = p.id
            LEFT JOIN opening_from_serial ofs ON ofs.product_id = p.id
        ),

        inflow AS (
            SELECT
                pd.product_id,
                COUNT(*) AS total_in
            FROM product_details pd
            WHERE pd.warehouse_id = :warehouseId
              AND pd.created_at BETWEEN :startDate AND :endDate
            GROUP BY pd.product_id
        ),

        outflow AS (
            SELECT
                pd.product_id,
                COUNT(*) AS total_out
            FROM product_details pd
            WHERE pd.warehouse_id = :warehouseId
              AND pd.outbound_order_item_id IS NOT NULL
              AND pd.updated_at BETWEEN :startDate AND :endDate
            GROUP BY pd.product_id
        )

        SELECT
            p.id        AS product_id,
            p.name      AS product_name,
            p.sku,
            w.id        AS warehouse_id,
            w.name      AS warehouse_name,
            o.opening_qty                       AS opening_stock,
            COALESCE(i.total_in, 0)             AS total_in,
            COALESCE(ofl.total_out, 0)          AS total_out,
            ( o.opening_qty
              + COALESCE(i.total_in, 0)
              - COALESCE(ofl.total_out, 0)
            ) AS closing_stock
        FROM products p
        JOIN warehouses w ON w.id = :warehouseId
        LEFT JOIN opening o ON o.product_id = p.id
        LEFT JOIN inflow i ON i.product_id = p.id
        LEFT JOIN outflow ofl ON ofl.product_id = p.id
        """;

        // 👉 CHỈ GẮN WHERE productId KHI CÓ
        if (productId != null) {
            sql += " WHERE p.id = :productId ";
        }

        sql += " ORDER BY p.name ";

        Query dataQuery = entityManager.createNativeQuery(
                sql + " LIMIT :limit OFFSET :offset"
        );

        dataQuery.setParameter("warehouseId", warehouseId);
        dataQuery.setParameter("startDate", startDate);
        dataQuery.setParameter("endDate", endDate);
        if (productId != null) {
            dataQuery.setParameter("productId", productId);
        }
        dataQuery.setParameter("limit", size);
        dataQuery.setParameter("offset", page * size);

        List<Object[]> rows = dataQuery.getResultList();

        List<InventorySummaryDto> content = rows.stream().map(r ->
                InventorySummaryDto.builder()
                        .productId((UUID) r[0])
                        .productName((String) r[1])
                        .sku((String) r[2])
                        .warehouseId((UUID) r[3])
                        .warehouseName((String) r[4])
                        .openingStock(((Number) r[5]).intValue())
                        .totalIn(((Number) r[6]).intValue())
                        .totalOut(((Number) r[7]).intValue())
                        .closingStock(((Number) r[8]).intValue())
                        .build()
        ).toList();

        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM (" + sql + ") t"
        );

        countQuery.setParameter("warehouseId", warehouseId);
        countQuery.setParameter("startDate", startDate);
        countQuery.setParameter("endDate", endDate);
        if (productId != null) {
            countQuery.setParameter("productId", productId);
        }

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }
}
