package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.entities.ProductDetail;

import java.util.UUID;

public interface IInventory {
    ProductDetailResponseDto scanAndPutToBin(String serialNumber);
    ProductDetailResponseDto moveSerialToBin(String serialNumber, UUID binId);
    String locate(String serialNumber);

}
