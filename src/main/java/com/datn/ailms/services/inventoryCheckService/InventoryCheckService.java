package com.datn.ailms.services.inventoryCheckService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.inventory_check.IInventoryCheckService;
import com.datn.ailms.mapper.InventoryCheckMapper;
import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckItemRequestDto;
import com.datn.ailms.model.dto.request.inventory_check.InventoryCheckRequestDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckItemResponseDto;
import com.datn.ailms.model.dto.response.inventory_check.InventoryCheckResponseDto;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.inventory_entities.InventoryCheck;
import com.datn.ailms.model.entities.inventory_entities.InventoryCheckItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.repositories.inventoryRepo.InventoryCheckItemRepository;
import com.datn.ailms.repositories.inventoryRepo.InventoryCheckRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCheckService implements IInventoryCheckService {

    final InventoryCheckRepository checkRepo;
    final InventoryCheckItemRepository itemRepo;
    final ProductDetailRepository productDetailRepo;
    final WarehouseRepository warehouseRepo;
    final UserRepository userRepository;
    final InventoryCheckMapper mapper;

    private String generateCheckCode() {
        long count = checkRepo.count() + 1;
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("INVCHK-%s-%04d", datePart, count);
    }

    @Override
    public List<InventoryCheckResponseDto> getAll() {
        return mapper.toResponseList(checkRepo.findAll());
    }

    @Override
    public InventoryCheckResponseDto getById(UUID id) {
        InventoryCheck check = checkRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        return mapper.toResponse(check);
    }

    // ✅ CREATE: TẠO HEADER + SNAPSHOT ITEM + SET IN_PROGRESS
    @Override
    public InventoryCheckResponseDto create(InventoryCheckRequestDto request) {
        try {
            Warehouse warehouse = warehouseRepo.findById(request.getWarehouseId())
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));
            User createdBy = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            // 1. Lưu Header trước
            InventoryCheck check = new InventoryCheck();
            check.setCode(generateCheckCode());
            check.setWarehouse(warehouse);
            check.setCreatedBy(createdBy);
            check.setNote(request.getNote());
            check.setDeadline(request.getDeadline());

            if (request.getCheckedBy() != null) {
                check.setCheckedBy(userRepository.findById(request.getCheckedBy()).orElse(createdBy));
            } else {
                check.setCheckedBy(createdBy);
            }

            check.setCreatedAt(LocalDateTime.now());
            check.setUpdatedAt(LocalDateTime.now());
            check.setStatus("IN_PROGRESS"); // Trạng thái đang kiểm kê

            InventoryCheck savedCheck = checkRepo.save(check);

            // 2. Lấy tồn kho (Snapshot)
            List<ProductDetail> allInWarehouse = productDetailRepo.findByWarehouseId(warehouse.getId());
            if (allInWarehouse == null) allInWarehouse = new ArrayList<>();

            List<SerialStatus> validStatuses = List.of(SerialStatus.IN_WAREHOUSE, SerialStatus.AVAILABLE, SerialStatus.IN_STOCK);
            List<InventoryCheckItem> itemsToSave = new ArrayList<>();

            for (ProductDetail pd : allInWarehouse) {
                if (pd.getStatus() != null && validStatuses.contains(pd.getStatus())) {
                    InventoryCheckItem item = InventoryCheckItem.builder()
                            .inventoryCheck(savedCheck)
                            .productDetail(pd)
                            .serialNumber(pd.getSerialNumber())
                            .systemQuantity(1)
                            .countedQuantity(0)
                            .status("UNKNOWN")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    itemsToSave.add(item);
                }
            }

            // 3. Lưu items xuống DB
            if (!itemsToSave.isEmpty()) {
                itemRepo.saveAll(itemsToSave);
            }

            return mapper.toResponse(savedCheck);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi BE khi tạo phiếu: " + e.getMessage());
        }
    }

    // DUMMY START (Không dùng nữa)
    @Override
    public InventoryCheckResponseDto startCheck(UUID checkId, UUID checkedByUserId) {
        return getById(checkId);
    }

    // ... (Giữ nguyên các hàm update, delete, complete, close, scan, suggest, getItem...)
    // Bạn copy lại phần thân các hàm này từ code cũ hoặc dùng lại code cũ, chỉ cần thay thế hàm create và Entity là được.

    @Override
    public InventoryCheckResponseDto update(UUID id, InventoryCheckRequestDto request) {
        InventoryCheck existing = checkRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        if (!"IN_PROGRESS".equals(existing.getStatus()) && !"DRAFT".equals(existing.getStatus()))
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        existing.setNote(request.getNote());
        existing.setDeadline(request.getDeadline());
        existing.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(checkRepo.save(existing));
    }
    @Override
    public void delete(UUID id) {
        if (!checkRepo.existsById(id)) throw new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND);
        checkRepo.deleteById(id);
    }
    @Override
    public InventoryCheckResponseDto completeCheck(UUID checkId) {
        InventoryCheck check = checkRepo.findById(checkId).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        if (!"IN_PROGRESS".equals(check.getStatus())) throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        List<InventoryCheckItem> items = itemRepo.findByInventoryCheckId(checkId);
        for (InventoryCheckItem item : items) {
            int sys = item.getSystemQuantity() != null ? item.getSystemQuantity() : 0;
            int cnt = item.getCountedQuantity() != null ? item.getCountedQuantity() : 0;
            if (sys == cnt) item.setStatus("MATCHED");
            else if (sys > cnt) item.setStatus("SHORTAGE");
            else item.setStatus("OVERAGE");
            item.setUpdatedAt(LocalDateTime.now());
        }
        itemRepo.saveAll(items);
        check.setStatus("PENDING_RECONCILIATION");
        check.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(checkRepo.save(check));
    }
    @Override
    public InventoryCheckResponseDto closeCheck(UUID checkId) {
        InventoryCheck check = checkRepo.findById(checkId).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        if (!"PENDING_RECONCILIATION".equals(check.getStatus())) throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        check.setStatus("CLOSED");
        check.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(checkRepo.save(check));
    }
    @Override
    public InventoryCheckItemResponseDto scanSerial(UUID checkId, String serialNumber, UUID scannedByUserId) {
        InventoryCheck check = checkRepo.findById(checkId).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        if (!"IN_PROGRESS".equals(check.getStatus())) throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        InventoryCheckItem item = itemRepo.findByInventoryCheckIdAndSerialNumber(checkId, serialNumber).orElse(null);
        if (item == null) {
            ProductDetail pd = productDetailRepo.findBySerialNumberIgnoreCase(serialNumber).orElse(null);
            item = InventoryCheckItem.builder()
                    .inventoryCheck(check).productDetail(pd).serialNumber(serialNumber)
                    .systemQuantity(0).countedQuantity(0).status("OVERAGE")
                    .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            item = itemRepo.save(item);
        }
        item.setCountedQuantity(item.getCountedQuantity() + 1);
        item.setCheckedTime(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        return mapper.toItemResponse(itemRepo.save(item));
    }
    @Override
    public List<String> suggestSerials(UUID checkId, String query) {
        if (query == null || query.trim().isEmpty()) return List.of();
        List<InventoryCheckItem> items = itemRepo.findByInventoryCheckId(checkId);
        String lower = query.trim().toLowerCase();
        return items.stream().map(InventoryCheckItem::getSerialNumber)
                .filter(s -> s != null && s.toLowerCase().contains(lower)).distinct().limit(10).collect(Collectors.toList());
    }
    @Override
    public List<InventoryCheckItemResponseDto> getItemsByCheckId(UUID checkId) {
        return itemRepo.findByInventoryCheckId(checkId).stream()
                .map(mapper::toItemResponse).collect(Collectors.toList());
    }
    @Override
    public InventoryCheckItemResponseDto addItemManual(UUID checkId, InventoryCheckItemRequestDto request) { return null; }
    @Override
    public InventoryCheckItemResponseDto updateItemManual(UUID itemId, InventoryCheckItemRequestDto request) { return null; }
    @Override
    public void deleteItemManual(UUID itemId) {
        InventoryCheckItem item = itemRepo.findById(itemId).orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));
        itemRepo.delete(item);
    }
}