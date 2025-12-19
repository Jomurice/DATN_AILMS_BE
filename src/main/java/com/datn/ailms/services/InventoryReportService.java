package com.datn.ailms.services;

import com.datn.ailms.model.dto.response.InventorySummaryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<InventorySummaryDto> getInventorySummary(
            LocalDate startDate,
            LocalDate endDate,
            UUID productId,
            UUID warehouseId,
            int page,
            int size
    ) {

        if (warehouseId == null) {
            throw new IllegalArgumentException("warehouseId là bắt buộc");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate phải trước endDate");
        }

        String baseSql = """
            WITH opening AS (
                SELECT 
                    p.id AS productId,
                    p.name AS productName,
                    p.sku,
                    :warehouseId AS warehouseId,
                    w.name AS warehouseName,
                    COALESCE(SUM(pois.scanned_quantity), 0)
                    - COALESCE(SUM(COALESCE(ooi.scanned_quantity, 0)), 0) AS openingStock
                FROM products p
                INNER JOIN warehouses w ON w.id = :warehouseId
                LEFT JOIN purchase_orders po
                    ON po.warehouse_id = :warehouseId
                    AND po.created_at < :startDate
                    AND po.status = 'COMPLETED'
                LEFT JOIN purchase_order_items pois
                    ON pois.purchase_order_id = po.id
                    AND pois.product_id = p.id
                LEFT JOIN outbound_order oo
                    ON oo.create_at < :startDate
                    AND oo.status = 'EXPORT'
                LEFT JOIN outbound_order_item ooi
                    ON ooi.outbound_order_id = oo.id
                    AND ooi.product_id = p.id
                LEFT JOIN product_details pd_out
                    ON pd_out.outbound_order_item_id = ooi.id
                    AND pd_out.warehouse_id = :warehouseId
                WHERE (CAST(:productId AS uuid) IS NULL OR p.id = CAST(:productId AS uuid))
                GROUP BY p.id, p.name, p.sku, w.id, w.name
            ),
            inflow AS (
                SELECT 
                    p.id AS productId,
                    p.name AS productName,
                    p.sku,
                    :warehouseId AS warehouseId,
                    w.name AS warehouseName,
                    COALESCE(SUM(pois.scanned_quantity), 0) AS totalIn
                FROM products p
                INNER JOIN warehouses w ON w.id = :warehouseId
                INNER JOIN purchase_orders po
                    ON po.warehouse_id = :warehouseId
                    AND po.created_at BETWEEN :startDate AND :endDate
                    AND po.status = 'COMPLETED'
                INNER JOIN purchase_order_items pois
                    ON pois.purchase_order_id = po.id
                    AND pois.product_id = p.id
                WHERE (CAST(:productId AS uuid) IS NULL OR p.id = CAST(:productId AS uuid))
                GROUP BY p.id, p.name, p.sku, w.id, w.name
            ),
            outflow AS (
                SELECT 
                    p.id AS productId,
                    p.name AS productName,
                    p.sku,
                    :warehouseId AS warehouseId,
                    w.name AS warehouseName,
                    COALESCE(SUM(ooi.scanned_quantity), 0) AS totalOut
                FROM products p
                INNER JOIN warehouses w ON w.id = :warehouseId
                INNER JOIN outbound_order oo
                    ON oo.create_at BETWEEN :startDate AND :endDate
                    AND oo.status = 'EXPORT'
                INNER JOIN outbound_order_item ooi
                    ON ooi.outbound_order_id = oo.id
                    AND ooi.product_id = p.id
                INNER JOIN product_details pd
                    ON pd.outbound_order_item_id = ooi.id
                    AND pd.warehouse_id = :warehouseId
                WHERE (CAST(:productId AS uuid) IS NULL OR p.id = CAST(:productId AS uuid))
                GROUP BY p.id, p.name, p.sku, w.id, w.name
            ),
            summary AS (
                SELECT 
                    COALESCE(o.productId, i.productId, out.productId, p.id) AS productId,
                    COALESCE(o.productName, i.productName, out.productName, p.name) AS productName,
                    COALESCE(o.sku, i.sku, out.sku, p.sku) AS sku,
                    :warehouseId AS warehouseId,
                    w.name AS warehouseName,
                    COALESCE(o.openingStock, 0) AS openingStock,
                    COALESCE(i.totalIn, 0) AS totalIn,
                    COALESCE(out.totalOut, 0) AS totalOut,
                    (COALESCE(o.openingStock, 0)
                     + COALESCE(i.totalIn, 0)
                     - COALESCE(out.totalOut, 0)) AS closingStock
                FROM products p
                INNER JOIN warehouses w ON w.id = :warehouseId
                LEFT JOIN opening o ON p.id = o.productId
                LEFT JOIN inflow i ON p.id = i.productId
                LEFT JOIN outflow out ON p.id = out.productId
                WHERE (CAST(:productId AS uuid) IS NULL OR p.id = CAST(:productId AS uuid))
            )
            SELECT *
            FROM summary
            WHERE closingStock >= 0
            ORDER BY productName
        """;

        // ===== DATA QUERY (paging)
        Query dataQuery = entityManager.createNativeQuery(
                baseSql + " LIMIT :limit OFFSET :offset"
        );
        dataQuery.setParameter("startDate", startDate);
        dataQuery.setParameter("endDate", endDate);
        dataQuery.setParameter("warehouseId", warehouseId);
        dataQuery.setParameter("productId", productId);
        dataQuery.setParameter("limit", size);
        dataQuery.setParameter("offset", page * size);

        @SuppressWarnings("unchecked")
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

        // ===== COUNT QUERY
        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM (" + baseSql + ") c"
        );
        countQuery.setParameter("startDate", startDate);
        countQuery.setParameter("endDate", endDate);
        countQuery.setParameter("warehouseId", warehouseId);
        countQuery.setParameter("productId", productId);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }
}
