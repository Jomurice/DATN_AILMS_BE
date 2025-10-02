package com.datn.ailms.services.inventoryService;

import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.request.inventory.ProductConfirmRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderItemRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductConfirmService implements IProductConfirmService {

    private final ProductDetailRepository productDetailRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final ProductDetailMapper productDetailMapper;
    private final PurchaseOrderItemRepository _poiRepo;

    @Override
    public ProductDetailResponseDto confirmSerial(ProductConfirmRequestDto request) {
        ProductDetail detail = productDetailRepository.findBySerialNumber(request.getSerialNumber())
                .orElseThrow(() -> new RuntimeException("Serial not found"));

        if (detail.getStatus() != SerialStatus.AVAILABLE) {
            throw new RuntimeException("Serial already scanned or in warehouse");
        }

        if (detail.getStatus() != SerialStatus.AVAILABLE) {
            throw new RuntimeException("Serial already scanned or in warehouse");
        }

        var warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        var user = userRepository.findById(request.getScannedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        detail.setWarehouse(warehouse);
        detail.setScannedBy(user);
        detail.setStatus(SerialStatus.IN_WAREHOUSE);
        detail.setUpdatedAt(LocalDateTime.now());


        productDetailRepository.save(detail);

        PurchaseOrderItem item = detail.getPurchaseOrderItem();
        if (item != null) {
            int scanned = productDetailRepository.countScannedByPurchaseOrderItem(item.getId());
            item.setScannedQuantity(scanned);
            _poiRepo.save(item);
        }



        return productDetailMapper.toResponse(detail);
    }
}
