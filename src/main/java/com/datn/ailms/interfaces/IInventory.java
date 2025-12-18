package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;

import java.util.UUID;

public interface IInventory {
    ProductDetailResponseDto scanAndPutToWarehouse(String serialNumber);
    ProductDetailResponseDto moveSerialToWarehouse(String serialNumber, UUID binId);
    String locate(String serialNumber);

}
