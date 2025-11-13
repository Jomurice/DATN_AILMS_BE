package com.datn.ailms.model.dto.request.checkInventory;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInventoryRequestDTO {
    @NotNull(message = "Warehouse ID không được null")
    private UUID warehouseId;  // Thêm: ID kho để tạo phiếu trong kho cụ thể

    @Size(max = 20, message = "Status quá dài")
    @Pattern(regexp = "(CHECKED|UNCHECKED)", message = "Status chỉ được CHECKED hoặc UNCHECKED")
    private String status;  // Optional, mặc định "UNCHECKED"

    @NotEmpty(message = "Serial numbers không được rỗng")
    @Size(min = 1, max = 100, message = "Số serial từ 1 đến 100")
    private Set<String> serialNumbers;  // Serial phải thuộc kho
}