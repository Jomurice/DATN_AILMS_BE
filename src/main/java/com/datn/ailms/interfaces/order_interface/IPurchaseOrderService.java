package com.datn.ailms.interfaces.order_interface;

import com.datn.ailms.model.dto.request.order.PurchaseOrderRequestDto;
import com.datn.ailms.model.dto.response.ProductDetailSerialDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderResponseDto;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

public interface IPurchaseOrderService {
    PurchaseOrderResponseDto create(PurchaseOrderRequestDto request);
    PurchaseOrderResponseDto getById(UUID id);
    List<PurchaseOrderResponseDto> getAll();
    PurchaseOrderResponseDto update(UUID id, PurchaseOrderRequestDto request);
    void delete(UUID id);
    void completeItem(UUID itemId,UUID userId); // ✅ khi hoàn tất -> tăng stock sản phẩm
    List<ProductDetailSerialDto> getSerials(UUID orderId, String sku);



    void generateQrCodeForPurchaseOrder(UUID purchaseOrderId, OutputStream os) throws WriterException, IOException;
}
