package com.datn.ailms.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class OutboundOrderFilter {
    private String search;      // code, name customer
    private String status;
//    private LocalDate fromDate;
//    private LocalDate toDate;
//    private UUID customerId;
}
