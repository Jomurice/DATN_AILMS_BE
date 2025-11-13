package com.datn.ailms.model.dto.request.checkInventory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInventoryItemRequestDTO {
    @NotNull(message = "Serial number không được null")
    @Size(max = 100, message = "Serial number quá dài")
    private String serialNumber;

    @NotNull(message = "Status không được null")
    @Pattern(regexp = "(CHECKED|UNCHECKED)", message = "Status chỉ được CHECKED hoặc UNCHECKED")
    @Size(max = 20, message = "Status quá dài")
    private String status;

    private LocalDateTime checkedTime;  // Optional
}