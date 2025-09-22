package com.datn.ailms.services.topoServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IInventory;
import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.mapper.ProductMapper;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventory {

    private final ProductDetailRepository detailRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final WarehouseRuleService warehouseRuleService; // thay cho BinRuleService
    private final ProductMapper pMapper;
    private final ProductDetailMapper pdMapper;

    @Override
    @Transactional
    public ProductDetailResponseDto scanAndPutToWarehouse(String serialNumber) {
        // 1. Tìm product detail
        ProductDetail detail = detailRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Serial not found: " + serialNumber));

        // 2. Nếu chưa gán product → tìm theo rule/prefix
        if (detail.getProduct() == null) {
            Product product = productRepo.findBySerialMatch(serialNumber)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            detail.setProduct(product);
        }

        // 3. Nếu chưa gán warehouse (bin/shelf/aisle...) → tìm warehouse theo rule
        if (detail.getWarehouse() == null) {
            Warehouse wh = warehouseRuleService.findWarehouseForSerial(serialNumber);

            if (wh.getCapacity() != null && wh.getCurrentQuantity() >= wh.getCapacity()) {
                throw new IllegalStateException("Target warehouse full: " + wh.getCode());
            }

            assignToWarehouse(detail, wh);
        }

        detailRepo.save(detail);
        return pdMapper.toResponse(detail);
    }

    @Override
    @Transactional
    public ProductDetailResponseDto moveSerialToWarehouse(String serialNumber, UUID warehouseId) {
        ProductDetail d = detailRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Serial not found: " + serialNumber));
        Warehouse target = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found: " + warehouseId));

        if (target.getCapacity() != null && target.getCapacity() <= target.getCurrentQuantity()) {
            throw new IllegalStateException("Target warehouse is full");
        }
        assignToWarehouse(d, target);
        detailRepo.save(d);
        return pdMapper.toResponse(d);
    }

    @Override
    @Transactional
    public String locate(String serialNumber) {
        ProductDetail d = detailRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Serial not found: " + serialNumber));
        if (d.getWarehouse() == null) return "NOT_ASSIGNED";

        // đi ngược từ node hiện tại lên cha
        Warehouse node = d.getWarehouse();
        StringBuilder path = new StringBuilder(node.getCode());
        while (node.getParent() != null) {
            node = node.getParent();
            path.insert(0, node.getCode() + " > ");
        }
        return path.toString();
    }

    private void assignToWarehouse(ProductDetail d, Warehouse target) {
        // giảm warehouse cũ nếu có
        if (d.getWarehouse() != null) {
            Warehouse old = d.getWarehouse();
            if (old.getCurrentQuantity() != null) {
                old.setCurrentQuantity(Math.max(0, old.getCurrentQuantity() - 1));
                warehouseRepo.save(old);
            }
        }
        // tăng warehouse mới
        if (target.getCapacity() != null) {
            target.setCurrentQuantity(target.getCurrentQuantity() + 1);
        }
        try {
            warehouseRepo.saveAndFlush(target);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalStateException("Warehouse updated concurrently, please retry");
        }
        d.setWarehouse(target);
        d.setStatus(SerialStatus.IN_STOCK);
    }
}
