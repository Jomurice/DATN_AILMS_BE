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
import com.datn.ailms.model.entities.topo_entities.*;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.warehousetopology.BinRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventory {

    private final ProductDetailRepository _detailRepo;
    private final ProductRepository _productRepo;      // thêm repo này
    private final BinRepository _binRepo;
    private final BinSelectorAutomatic _binSelector;
    private final BinRuleService _binRuleService;
    private final ProductMapper _pMapper;
    private final ProductDetailMapper _pdMapper;

    @Override
    @Transactional
    public ProductDetailResponseDto scanAndPutToBin(String serialNumber) {
        // 1. Tìm hoặc tạo ProductDetail
        ProductDetail detail = _detailRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Serial not found: " + serialNumber));
//        String prefix = extractPrefix(serialNumber);
        // 2. Nếu chưa gán product → tìm theo prefix
        if (detail.getProduct() == null) {
            Product product = _productRepo.findBySerialMatch(serialNumber)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            detail.setProduct(product);
        }

        // 3. Nếu chưa gán bin → tìm bin
        if (detail.getBin() == null) {
            Bin bin = _binRuleService.findBinForSerial(serialNumber);

            if (bin.getCurrentQty() >= bin.getCapacity()) {
                throw new IllegalStateException("Target bin full: " + bin.getCode());
            }

            // cập nhật capacity
            bin.setCurrentQty(bin.getCurrentQty() + 1);
            _binRepo.save(bin);

            // gán vào detail
            detail.setBin(bin);
            detail.setStatus(SerialStatus.IN_STOCK);
        }

        _detailRepo.save(detail);
        return _pdMapper.toResponse(detail);
    }

    @Override
    @Transactional
    public ProductDetailResponseDto moveSerialToBin(String serialNumber, UUID binId) {
        ProductDetail d = _detailRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Serial not found: " + serialNumber));
        Bin target = _binRepo.findById(binId)
                .orElseThrow(() -> new EntityNotFoundException("Bin not found: " + binId));

        if (target.getCapacity() <= target.getCurrentQty()) {
            throw new IllegalStateException("Target bin is full");
        }
        assignToBin(d, target);
        _detailRepo.save(d);
        return _pdMapper.toResponse(d);
    }

    @Override
    @Transactional
    public String locate(String serialNumber) {
        ProductDetail d = _detailRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Serial not found: " + serialNumber));
        if (d.getBin() == null) return "NOT_ASSIGNED";

        Bin b = d.getBin();
        Shelf s = b.getShelf();
        Aisle a = s.getAisle();
        Zone z = a.getZone();
        Warehouse w = z.getWarehouse();
        return String.format("%s > %s > %s > %s > %s",
                w.getCode(), z.getCode(), a.getCode(), s.getCode(), b.getCode());
    }

    private void assignToBin(ProductDetail d, Bin target) {
        // giảm bin cũ nếu có
        if (d.getBin() != null) {
            Bin old = d.getBin();
            old.setCurrentQty(Math.max(0, old.getCurrentQty() - 1));
            _binRepo.save(old);
        }
        // tăng bin mới
        target.setCurrentQty(target.getCurrentQty() + 1);
        try {
            _binRepo.saveAndFlush(target);
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalStateException("Bin updated concurrently, please retry");
        }
        d.setBin(target);
        d.setStatus(SerialStatus.IN_STOCK);
    }

    private String extractPrefix(String serialNumber) {
        // Giả sử hãng Apple, Samsung luôn có 5-6 ký tự đầu là prefix
        if (serialNumber.length() < 5) {
            throw new IllegalArgumentException("Serial number không hợp lệ: " + serialNumber);
        }
        return serialNumber.substring(0, 6).toUpperCase();
    }
}
