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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public Page<InventoryCheckResponseDto> getAll(String status, Pageable pageable) {
        if (status == null || status.trim().isEmpty() || "ALL".equalsIgnoreCase(status)) {
            return checkRepo.findAll(pageable).map(mapper::toResponse);
        }
        return checkRepo.findByStatus(status, pageable).map(mapper::toResponse);
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

    @Override
    public InventoryCheckResponseDto create(InventoryCheckRequestDto request) {
        try {
            Warehouse warehouse = warehouseRepo.findById(request.getWarehouseId())
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));
            User createdBy = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            InventoryCheck check = mapper.toEntity(request);
            check.setCode(generateCheckCode());
            check.setWarehouse(warehouse);
            check.setCreatedBy(createdBy);
            if (request.getCheckedBy() != null) {
                check.setCheckedBy(userRepository.findById(request.getCheckedBy()).orElse(createdBy));
            } else {
                check.setCheckedBy(createdBy);
            }
            check.setCreatedAt(LocalDateTime.now());
            check.setUpdatedAt(LocalDateTime.now());
            check.setStatus("DRAFT");

            // 🔥 SỬA: BẮT BUỘC DEADLINE LÀ HẾT NGÀY HÔM NAY (23:59:59)
            // Backend tự set, không quan tâm FE gửi ngày nào
            check.setDeadline(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));

            InventoryCheck savedCheck = checkRepo.save(check);

            // Snapshot dữ liệu
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

            if (!itemsToSave.isEmpty()) {
                itemRepo.saveAll(itemsToSave);
            }

            return mapper.toResponse(savedCheck);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi tạo phiếu: " + e.getMessage());
        }
    }

    // --- SCAN SERIAL (GIỮ NGUYÊN LOGIC CHẶN HÀNG THỪA) ---
    @Override
    public InventoryCheckItemResponseDto scanSerial(UUID checkId, String serialNumber, UUID scannedByUserId) {
        InventoryCheck check = checkRepo.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        // 1. Tự động chuyển trạng thái
        if ("DRAFT".equals(check.getStatus())) {
            check.setStatus("IN_PROGRESS");
            check.setUpdatedAt(LocalDateTime.now());
            checkRepo.save(check);
        } else if (!"IN_PROGRESS".equals(check.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        String cleanSerial = serialNumber.trim();

        // 2. Tìm Item trong phiếu
        InventoryCheckItem item = itemRepo.findByInventoryCheckIdAndSerialNumber(checkId, cleanSerial).orElse(null);

        if (item != null && item.getCountedQuantity() >= 1) {
            // Nếu đã đếm rồi -> Báo lỗi
            throw new RuntimeException("SERIAL_ALREADY_SCANNED");
        }

        // 3. Lấy thông tin người quét
        User scanner = null;
        if (scannedByUserId != null) {
            scanner = userRepository.findById(scannedByUserId).orElse(null);
        }

        // 4. Xử lý Item (CHẶN HÀNG THỪA)
        if (item == null) {
            // Thay vì tạo mới status OVERAGE, ta chặn luôn
            throw new RuntimeException("Lỗi: Serial này không có trong sổ sách tồn kho của kho này");
        }

        // 5. Tăng số lượng (Từ 0 lên 1)
        item.setCountedQuantity(1);
        item.setCheckedTime(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        if (scanner != null) {
            item.setScannedBy(scanner);
        }
        return mapper.toItemResponse(itemRepo.save(item));
    }

    @Override
    public InventoryCheckResponseDto startCheck(UUID checkId, UUID checkedByUserId) { return getById(checkId); }

    @Override
    public InventoryCheckResponseDto update(UUID id, InventoryCheckRequestDto request) {
        InventoryCheck existing = checkRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        if (!"DRAFT".equals(existing.getStatus()) && !"IN_PROGRESS".equals(existing.getStatus()))
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        existing.setNote(request.getNote()); existing.setDeadline(request.getDeadline()); existing.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(checkRepo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        InventoryCheck check = checkRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        // ⚠️ BẮT BUỘC CÓ: Chặn xóa nếu không phải là Nháp
        if (!"DRAFT".equals(check.getStatus())) {
            throw new RuntimeException("Chỉ được xóa phiếu ở trạng thái NHÁP (DRAFT).");
        }

        // Xóa các dòng chi tiết trước (để tránh lỗi database)
        List<InventoryCheckItem> items = itemRepo.findByInventoryCheckId(id);
        itemRepo.deleteAll(items);

        checkRepo.delete(check);
    }
    @Override
    public InventoryCheckResponseDto completeCheck(UUID checkId) {
        InventoryCheck check = checkRepo.findById(checkId).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        if (!"IN_PROGRESS".equals(check.getStatus())) throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        List<InventoryCheckItem> items = itemRepo.findByInventoryCheckId(checkId);
        for (InventoryCheckItem item : items) {
            int sys = item.getSystemQuantity() != null ? item.getSystemQuantity() : 0;
            int cnt = item.getCountedQuantity() != null ? item.getCountedQuantity() : 0;

            // Logic cũ vẫn giữ để tính toán
            if (sys == cnt) item.setStatus("MATCHED");
            else if (sys > cnt) item.setStatus("SHORTAGE");
            else item.setStatus("OVERAGE");

            item.setUpdatedAt(LocalDateTime.now());
        }
        itemRepo.saveAll(items);
        check.setStatus("PENDING_RECONCILIATION"); check.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(checkRepo.save(check));
    }

    @Override
    public InventoryCheckResponseDto closeCheck(UUID checkId) {
        InventoryCheck check = checkRepo.findById(checkId).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));
        if (!"PENDING_RECONCILIATION".equals(check.getStatus())) throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        check.setStatus("CLOSED"); check.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(checkRepo.save(check));
    }
    @Override
    public List<String> suggestSerials(UUID checkId, String query) {
        if (query == null || query.trim().isEmpty()) return List.of();
        return itemRepo.findByInventoryCheckId(checkId).stream().map(InventoryCheckItem::getSerialNumber)
                .filter(s -> s != null && s.toLowerCase().contains(query.trim().toLowerCase())).distinct().limit(10).collect(Collectors.toList());
    }
    @Override
    public List<InventoryCheckItemResponseDto> getItemsByCheckId(UUID checkId) {
        return itemRepo.findByInventoryCheckId(checkId).stream().map(mapper::toItemResponse).collect(Collectors.toList());
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