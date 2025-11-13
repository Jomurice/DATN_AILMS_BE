package com.datn.ailms.services.checkInventory;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.ICheckInventoryService;
import com.datn.ailms.mapper.CheckInventoryMapper;
import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryItemRequestDTO;
import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryRequestDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryItemResponseDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryResponseDTO;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.checkInventory_entities.CheckInventory;
import com.datn.ailms.model.entities.checkInventory_entities.CheckInventoryItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.repositories.checkInventoryRepo.CheckInventoryItemRepository;
import com.datn.ailms.repositories.checkInventoryRepo.CheckInventoryRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckInventoryService implements ICheckInventoryService {

    private final CheckInventoryRepository checkInventoryRepository;
    private final CheckInventoryItemRepository checkInventoryItemRepository;
    private final ProductDetailRepository productDetailRepository;
    private final WarehouseRepository warehouseRepository;
    private final CheckInventoryMapper checkInventoryMapper;

    @Override
    public CheckInventoryResponseDTO create(CheckInventoryRequestDTO dto, User currentUser) {
        if (dto.getSerialNumbers() == null || dto.getSerialNumbers().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);  // Fix: Bỏ string message, dùng message từ ErrorCode
        }
        if (dto.getWarehouseId() == null) {
            throw new AppException(ErrorCode.WAREHOUSE_NOT_FOUND);  // Fix: Bỏ string
        }

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));

        Set<ProductDetail> productDetails = dto.getSerialNumbers().stream()
                .map(serial -> productDetailRepository.findBySerialNumber(serial)
                        .filter(pd -> pd.getWarehouse().getId().equals(dto.getWarehouseId()))
                        .orElseThrow(() -> new AppException(ErrorCode.INVALID_INPUT)))
                .collect(Collectors.toSet());

        CheckInventory checkInventory = checkInventoryMapper.toEntity(dto);
        checkInventory.setCreatedBy(currentUser.getId());
        checkInventory.setWarehouse(warehouse);
        checkInventory.getProductDetails().addAll(productDetails);

        productDetails.forEach(pd -> {
            CheckInventoryItem item = checkInventoryMapper.toItemEntity(CheckInventoryItemRequestDTO.builder()
                    .serialNumber(pd.getSerialNumber())
                    .status("UNCHECKED")
                    .build());
            item.setCheckInventory(checkInventory);
            item.setProductDetails(pd);
            checkInventory.getItems().add(item);
        });

        CheckInventory saved = checkInventoryRepository.save(checkInventory);
        return checkInventoryMapper.toResponse(saved);
    }

    @Override
    public CheckInventoryResponseDTO update(UUID id, CheckInventoryRequestDTO dto, User currentUser) {
        CheckInventory entity = checkInventoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CHECK_INVENTORY_NOT_FOUND));  // Fix: Bỏ string

        if (dto.getStatus() != null && ("CHECKED".equals(dto.getStatus()) || "UNCHECKED".equals(dto.getStatus()))) {
            entity.setStatus(dto.getStatus());
        }
        entity.setCheckedBy(currentUser.getId());

        CheckInventory saved = checkInventoryRepository.save(entity);
        return checkInventoryMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!checkInventoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.CHECK_INVENTORY_NOT_FOUND);  // Fix: Bỏ string
        }
        CheckInventory entity = checkInventoryRepository.findById(id).orElseThrow();
        entity.setStatus("DELETED");
        checkInventoryRepository.save(entity);
    }

    @Override
    public CheckInventoryResponseDTO findById(UUID id) {
        CheckInventory entity = checkInventoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CHECK_INVENTORY_NOT_FOUND));  // Fix: Bỏ string
        return checkInventoryMapper.toResponse(entity);
    }

    @Override
    public List<CheckInventoryResponseDTO> findAll() {
        return checkInventoryMapper.toResponseList(checkInventoryRepository.findAll());
    }

    @Override
    public List<CheckInventoryResponseDTO> findByWarehouseLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);  // Fix: Bỏ string
        }
        List<Warehouse> rootWarehouses = warehouseRepository.findByLocationAndParentIsNull(location);
        List<UUID> warehouseIds = rootWarehouses.stream().map(Warehouse::getId).collect(Collectors.toList());
        List<CheckInventory> inventories = checkInventoryRepository.findByWarehouseIdIn(warehouseIds);
        return checkInventoryMapper.toResponseList(inventories);
    }

    @Override
    public CheckInventoryItemResponseDTO updateItem(UUID checkId, CheckInventoryItemRequestDTO dto, User currentUser) {
        CheckInventory checkInventory = checkInventoryRepository.findById(checkId)
                .orElseThrow(() -> new AppException(ErrorCode.CHECK_INVENTORY_NOT_FOUND));  // Fix: Bỏ string

        CheckInventoryItem item = checkInventoryItemRepository.findBySerialNumberAndCheckInventoryId(dto.getSerialNumber(), checkId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_INPUT));  // Fix: Bỏ string

        String newStatus = dto.getStatus() != null && "CHECKED".equals(dto.getStatus()) ? "CHECKED" : "UNCHECKED";
        item.setStatus(newStatus);
        if ("CHECKED".equals(newStatus)) {
            item.setCheckedTime(dto.getCheckedTime() != null ? dto.getCheckedTime() : LocalDateTime.now());
        }

        if (checkInventory.getCheckedBy() == null) {
            checkInventory.setCheckedBy(currentUser.getId());
        }

        CheckInventoryItem savedItem = checkInventoryItemRepository.save(item);
        checkInventoryRepository.save(checkInventory);

        updateCheckInventoryStatus(checkInventory);

        return checkInventoryMapper.toItemResponse(savedItem);
    }

    private void updateCheckInventoryStatus(CheckInventory checkInventory) {
        boolean allChecked = checkInventory.getItems().stream()
                .allMatch(item -> "CHECKED".equals(item.getStatus()));
        checkInventory.setStatus(allChecked ? "CHECKED" : "UNCHECKED");
        checkInventoryRepository.save(checkInventory);
    }
}