//package com.datn.ailms.repositories;
//
//import com.datn.ailms.model.dto.response.InventorySummaryDto;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public interface InventoryReportRepository {
//
//    @Query(value = """
//        WITH opening AS (
//            SELECT
//                p.id AS productId,
//                p.name AS productName,
//                p.sku,
//                :warehouseId AS warehouseId,
//                w.name AS warehouseName,
//                COALESCE(SUM(pois.scanned_quantity), 0) - COALESCE(SUM(COALESCE(ooi.scanned_quantity, 0)), 0) AS openingStock
//            FROM products p
//            INNER JOIN warehouses w ON w.id = :warehouseId
//            LEFT JOIN purchase_orders po ON po.warehouse_id = :warehouseId AND po.created_at < :startDate AND po.status = 'COMPLETED'
//            LEFT JOIN purchase_order_items pois ON pois.purchase_order_id = po.id AND pois.product_id = p.id
//            LEFT JOIN outbound_order oo ON oo.create_at < :startDate AND oo.status = 'EXPORT'
//            LEFT JOIN outbound_order_item ooi ON ooi.outbound_order_id = oo.id AND ooi.product_id = p.id
//            LEFT JOIN product_details pd_out ON pd_out.outbound_order_item_id = ooi.id AND pd_out.warehouse_id = :warehouseId
//            WHERE (:productId IS NULL OR p.id = :productId)
//            GROUP BY p.id, p.name, p.sku, w.id, w.name
//        ),
//        inflow AS (
//            SELECT
//                p.id AS productId,
//                p.name AS productName,
//                p.sku,
//                :warehouseId AS warehouseId,
//                w.name AS warehouseName,
//                COALESCE(SUM(pois.scanned_quantity), 0) AS totalIn
//            FROM products p
//            INNER JOIN warehouses w ON w.id = :warehouseId
//            INNER JOIN purchase_orders po ON po.warehouse_id = :warehouseId AND po.created_at >= :startDate AND po.created_at <= :endDate AND po.status = 'COMPLETED'
//            INNER JOIN purchase_order_items pois ON pois.purchase_order_id = po.id AND pois.product_id = p.id
//            WHERE (:productId IS NULL OR p.id = :productId)
//            GROUP BY p.id, p.name, p.sku, w.id, w.name
//        ),
//        outflow AS (
//            SELECT
//                p.id AS productId,
//                p.name AS productName,
//                p.sku,
//                :warehouseId AS warehouseId,
//                w.name AS warehouseName,
//                COALESCE(SUM(ooi.scanned_quantity), 0) AS totalOut
//            FROM products p
//            INNER JOIN warehouses w ON w.id = :warehouseId
//            INNER JOIN outbound_order oo ON oo.create_at >= :startDate AND oo.create_at <= :endDate AND oo.status = 'EXPORT'
//            INNER JOIN outbound_order_item ooi ON ooi.outbound_order_id = oo.id AND ooi.product_id = p.id
//            INNER JOIN product_details pd ON pd.outbound_order_item_id = ooi.id AND pd.warehouse_id = :warehouseId
//            WHERE (:productId IS NULL OR p.id = :productId)
//            GROUP BY p.id, p.name, p.sku, w.id, w.name
//        ),
//        summary AS (
//            SELECT
//                COALESCE(o.productId, i.productId, out.productId, p.id) AS productId,
//                COALESCE(o.productName, i.productName, out.productName, p.name) AS productName,
//                COALESCE(o.sku, i.sku, out.sku, p.sku) AS sku,
//                :warehouseId AS warehouseId,
//                w.name AS warehouseName,
//                COALESCE(o.openingStock, 0) AS openingStock,
//                COALESCE(i.totalIn, 0) AS totalIn,
//                COALESCE(out.totalOut, 0) AS totalOut,
//                (COALESCE(o.openingStock, 0) + COALESCE(i.totalIn, 0) - COALESCE(out.totalOut, 0)) AS closingStock
//            FROM products p
//            INNER JOIN warehouses w ON w.id = :warehouseId
//            LEFT JOIN opening o ON p.id = o.productId
//            LEFT JOIN inflow i ON p.id = i.productId
//            LEFT JOIN outflow out ON p.id = out.productId
//            WHERE (:productId IS NULL OR p.id = :productId)
//        )
//        SELECT
//            productId, productName, sku, warehouseId, warehouseName,
//            openingStock, totalIn, totalOut, closingStock
//        FROM summary
//        WHERE closingStock >= 0
//        ORDER BY productName
//        """, nativeQuery = true)
//    List<InventorySummaryDto> getInventorySummaryForWarehouse(
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("productId") UUID productId,
//            @Param("warehouseId") UUID warehouseId
//    );
//}