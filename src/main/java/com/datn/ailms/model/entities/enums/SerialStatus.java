package com.datn.ailms.model.entities.enums;

public enum SerialStatus {
   NEW, INBOUND, IN_STOCK, RESERVED, IN_TRANSIT, SHIPPED, RETURNED,OUTBOUND,
   AVAILABLE,  // mới tạo, chưa nhập kho
   SCANNED,    // đã quét
   IN_WAREHOUSE, // đã nhập kho
   LOST
}
