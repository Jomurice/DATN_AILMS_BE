package com.datn.ailms.services.inventoryService;

import com.datn.ailms.interfaces.IProductScanService;
import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.request.inventory.ProductGenerateSerialRequest;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderItemRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductScanService implements IProductScanService {


    private final ProductDetailRepository productDetailRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final ProductDetailMapper _pdMapper;

    @Override
    public List<ProductDetailResponseDto> generateSerials(ProductGenerateSerialRequest request) {
        var poItem = purchaseOrderItemRepository.findById(request.getPurchaseOrderItemId())
                .orElseThrow(() -> new RuntimeException("PO Item not found"));
        var product = poItem.getProduct();
        var user = userRepository.findById(request.getScannedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        int quantity = poItem.getOrderQuantity() != null ? poItem.getOrderQuantity() : 1;

        List<ProductDetail> details = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            String serial = product.getSerialPrefix() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // đảm bảo serial unique
            while (productDetailRepository.existsBySerialNumber(serial)) {
                serial = product.getSerialPrefix() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            }

            ProductDetail detail = ProductDetail.builder()
                    .serialNumber(serial)
                    .product(product)
                    .purchaseOrderItem(poItem)
                    .scannedBy(user)
                    .status(SerialStatus.AVAILABLE)
                    .createdAt(LocalDateTime.now())
                    .build();

            details.add(detail);
        }

        // Lưu tất cả cùng lúc
        List<ProductDetail> saved = productDetailRepository.saveAll(details);

        // Chuyển sang DTO list
        return _pdMapper.toResponseList(saved);
    }

    @Transactional
    public List<ProductDetailResponseDto> generateSerialBatch(ProductGenerateSerialRequest request, int quantity) {
        var poItem = purchaseOrderItemRepository.findById(request.getPurchaseOrderItemId())
                .orElseThrow(() -> new RuntimeException("PO Item not found"));
        var product = poItem.getProduct();;
        var user = userRepository.findById(request.getScannedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ProductDetail> details = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            String serial = product.getSerialPrefix() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            while (productDetailRepository.existsBySerialNumber(serial)) {
                serial = product.getSerialPrefix() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            }

            ProductDetail detail = ProductDetail.builder()
                    .serialNumber(serial)
                    .product(product)
                    .purchaseOrderItem(poItem)
                    .scannedBy(user)
                    .status(SerialStatus.AVAILABLE)
                    .createdAt(LocalDateTime.now())
                    .build();

            details.add(detail);
        }

        List<ProductDetail> saved = productDetailRepository.saveAll(details);

        return _pdMapper.toResponseList(saved);
    }
}
