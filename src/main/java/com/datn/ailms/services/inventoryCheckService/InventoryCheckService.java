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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

    // --- Helper: Generate Code ---
    private String generateCheckCode() {
        long count = checkRepo.count() + 1;
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("INVCHK-%s-%04d", datePart, count);
    }

    // --- CRUD Phiếu Kiểm Kê ---

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
        Warehouse warehouse = warehouseRepo.findById(request.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));
        User createdBy = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        InventoryCheck check = mapper.toEntity(request);
        check.setCode(generateCheckCode());
        check.setWarehouse(warehouse);
        check.setCreatedBy(createdBy);
        check.setCreatedAt(LocalDateTime.now());
        check.setUpdatedAt(LocalDateTime.now());
        check.setStatus("DRAFT");

        if (request.getCheckedBy() != null) {
            User checkedBy = userRepository.findById(request.getCheckedBy())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            check.setCheckedBy(checkedBy);
        }
        return mapper.toResponse(checkRepo.save(check));
    }

    @Override
    public InventoryCheckResponseDto update(UUID id, InventoryCheckRequestDto request) {
        InventoryCheck existing = checkRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        if (!"DRAFT".equals(existing.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        existing.setWarehouse(warehouseRepo.findById(request.getWarehouseId()).orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED)));
        existing.setNote(request.getNote());
        existing.setDeadline(request.getDeadline());

        if (request.getCheckedBy() != null) {
            existing.setCheckedBy(userRepository.findById(request.getCheckedBy()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        }
        existing.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(checkRepo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        if (!checkRepo.existsById(id)) throw new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND);
        checkRepo.deleteById(id);
    }

    // --- Quản lý Luồng Kiểm Kê ---

    @Override
    public InventoryCheckResponseDto startCheck(UUID checkId, UUID checkedByUserId) {
        InventoryCheck check = checkRepo.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        if (!"DRAFT".equals(check.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        if (check.getCheckedBy() == null && checkedByUserId != null) {
            check.setCheckedBy(userRepository.findById(checkedByUserId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        }

        // 1. Lấy tất cả Serial đang IN_WAREHOUSE trong kho cần kiểm kê (SNAPSHOT)
        List<ProductDetail> serialsInWarehouse = productDetailRepo.findByWarehouseIdAndStatus(
                check.getWarehouse().getId(), SerialStatus.IN_WAREHOUSE.name());

        Set<InventoryCheckItem> checkItems = new HashSet<>();
        for (ProductDetail pd : serialsInWarehouse) {
            // 2. Tạo Check Item cho từng Serial tồn tại (system_quantity = 1)
            InventoryCheckItem item = InventoryCheckItem.builder()
                    .inventoryCheck(check)
                    .productDetail(pd)
                    .serialNumber(pd.getSerialNumber())
                    .systemQuantity(1)
                    .countedQuantity(0)
                    .status("UNKNOWN")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            checkItems.add(item);
        }

        check.setItems(checkItems);
        check.setStatus("IN_PROGRESS");
        check.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(checkRepo.save(check));
    }

    @Override
    public InventoryCheckResponseDto completeCheck(UUID checkId) {
        InventoryCheck check = checkRepo.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        if (!"IN_PROGRESS".equals(check.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        for (InventoryCheckItem item : check.getItems()) {
            if (item.getSystemQuantity().equals(item.getCountedQuantity())) {
                item.setStatus("MATCHED");
            } else if (item.getSystemQuantity() > item.getCountedQuantity()) {
                item.setStatus("SHORTAGE");
            } else {
                item.setStatus("OVERAGE");
            }
            item.setUpdatedAt(LocalDateTime.now());
        }

        // SỬA ĐỔI: Chuyển sang trạng thái Chờ Xử lý Chênh lệch
        check.setStatus("PENDING_RECONCILIATION");
        check.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(checkRepo.save(check));
    }

    @Override
    public InventoryCheckResponseDto closeCheck(UUID checkId) {
        InventoryCheck check = checkRepo.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        // Kiểm tra trạng thái hiện tại phải là PENDING_RECONCILIATION
        if (!"PENDING_RECONCILIATION".equals(check.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        check.setStatus("CLOSED");
        check.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(checkRepo.save(check));
    }


    // --- Quét Serial (Check Count) ---

    private InventoryCheckItem createOverageItem(InventoryCheck check, String serialNumber, ProductDetail productDetail) {

        InventoryCheckItem item = InventoryCheckItem.builder()
                .inventoryCheck(check)
                .productDetail(productDetail)
                .serialNumber(serialNumber)
                .systemQuantity(0)
                .countedQuantity(0)
                .status("OVERAGE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        check.getItems().add(item);
        return item;
    }

    @Override
    public List<String> suggestSerials(UUID checkId, String query) {
        if (query == null || query.trim().isEmpty()) return List.of();

        InventoryCheck check = checkRepo.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        String lowerQuery = query.trim().toLowerCase();

        return check.getItems().stream()
                .map(InventoryCheckItem::getSerialNumber)
                .filter(serial -> serial != null && serial.toLowerCase().contains(lowerQuery))
                .distinct()
                .limit(10) // Giới hạn kết quả trả về
                .collect(Collectors.toList());
    }

    @Override
    public InventoryCheckItemResponseDto scanSerial(UUID checkId, String serialNumber, UUID scannedByUserId) {
        InventoryCheck check = checkRepo.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        if (!"IN_PROGRESS".equals(check.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        ProductDetail productDetail = productDetailRepo.findBySerialNumberIgnoreCase(serialNumber)
                .orElse(null);

        InventoryCheckItem item = itemRepo.findByInventoryCheckIdAndSerialNumber(checkId, serialNumber)
                .orElse(null);

        if (item == null) {
            item = createOverageItem(check, serialNumber, productDetail);
        }

        item.setCountedQuantity(item.getCountedQuantity() + 1);
        item.setCheckedTime(LocalDateTime.now());
        item.setStatus("IN_PROGRESS");
        item.setUpdatedAt(LocalDateTime.now());

        return mapper.toItemResponse(itemRepo.save(item));
    }

    // --- Chi tiết Item (Manual CRUD) ---

    @Override
    public List<InventoryCheckItemResponseDto> getItemsByCheckId(UUID checkId) {
        // FIX: Gọi Repository để sử dụng phương thức
        List<InventoryCheckItem> items = itemRepo.findByInventoryCheckId(checkId);

        return items.stream()
                .map(mapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryCheckItemResponseDto addItemManual(UUID checkId, InventoryCheckItemRequestDto request) {
        InventoryCheck check = checkRepo.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_CHECK_NOT_FOUND));

        if (!"DRAFT".equals(check.getStatus()) && !"IN_PROGRESS".equals(check.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        if (itemRepo.findByInventoryCheckIdAndSerialNumber(checkId, request.getSerialNumber()).isPresent()) {
            throw new AppException(ErrorCode.SERIAL_ALREADY_SCANNED);
        }

        ProductDetail pd = productDetailRepo.findBySerialNumberIgnoreCase(request.getSerialNumber()).orElse(null);

        InventoryCheckItem newItem = InventoryCheckItem.builder()
                .inventoryCheck(check)
                .productDetail(pd)
                .serialNumber(request.getSerialNumber())
                .countedQuantity(request.getCountedQuantity() != null ? request.getCountedQuantity() : 0)
                .systemQuantity(0)
                .status("OVERAGE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapper.toItemResponse(itemRepo.save(newItem));
    }

    @Override
    public InventoryCheckItemResponseDto updateItemManual(UUID itemId, InventoryCheckItemRequestDto request) {
        InventoryCheckItem existing = itemRepo.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

        if (!"DRAFT".equals(existing.getInventoryCheck().getStatus()) && !"IN_PROGRESS".equals(existing.getInventoryCheck().getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        existing.setCountedQuantity(request.getCountedQuantity() != null ? request.getCountedQuantity() : existing.getCountedQuantity());
        existing.setNote(request.getNote() != null ? request.getNote() : existing.getNote());
        existing.setUpdatedAt(LocalDateTime.now());

        return mapper.toItemResponse(itemRepo.save(existing));
    }

    @Override
    public void deleteItemManual(UUID itemId) {
        InventoryCheckItem existing = itemRepo.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

        if (!"DRAFT".equals(existing.getInventoryCheck().getStatus()) && !"IN_PROGRESS".equals(existing.getInventoryCheck().getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
        itemRepo.delete(existing);
    }
}