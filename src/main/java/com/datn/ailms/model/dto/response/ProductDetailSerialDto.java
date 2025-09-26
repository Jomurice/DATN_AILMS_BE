package com.datn.ailms.model.dto.response;

import com.datn.ailms.model.entities.enums.SerialStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailSerialDto {
    private String serialNumber;
    private SerialStatus status;
    private String binId;
    private String sku;
    private UUID productId;

    public ProductDetailSerialDto(UUID productId, String serialNumber, String sku) {
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.sku = sku;
    }
}
