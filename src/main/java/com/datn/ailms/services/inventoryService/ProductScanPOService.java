package com.datn.ailms.services.inventoryService;

import com.datn.ailms.interfaces.IProductScanPOService;
import com.datn.ailms.model.dto.request.inventory.GenerateSerialForPORequest;
import com.datn.ailms.model.dto.response.inventory.GenerateSerialForPOResponse;
import com.datn.ailms.model.dto.response.inventory.POItemSerialsResponse;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductScanPOService implements IProductScanPOService {

    private final PurchaseOrderRepository poRepo;
    private final ProductDetailRepository productDetailRepo;
    private final UserRepository userRepo;

    @Override
    public GenerateSerialForPOResponse generateSerialsForPO(GenerateSerialForPORequest request) {
        var po = poRepo.findById(request.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));

        var user = userRepo.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<POItemSerialsResponse> itemsSerials = new ArrayList<>();

        for (PurchaseOrderItem item : po.getItems()) {
            List<String> serialNumbers = new ArrayList<>();

            for (int i = 0; i < item.getOrderQuantity(); i++) {
                String serial = item.getProduct().getSerialPrefix() + "-" +
                        UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                ProductDetail detail = ProductDetail.builder()
                        .serialNumber(serial)
                        .product(item.getProduct())
                        .purchaseOrderItem(item)
                        .scannedBy(user)
                        .status(SerialStatus.AVAILABLE)
                        .createdAt(LocalDateTime.now())
                        .build();

                productDetailRepo.save(detail);
                serialNumbers.add(serial);
            }

            itemsSerials.add(POItemSerialsResponse.builder()
                    .poItemId(item.getId())
                    .productId(item.getProduct().getId())
                    .serialNumbers(serialNumbers)
                    .build());
        }


        return GenerateSerialForPOResponse.builder()
                .purchaseOrderId(po.getId())
                .itemsSerials(itemsSerials)
                .build();

    }
}
