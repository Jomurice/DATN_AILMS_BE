package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.inventory.GenerateSerialForPORequest;
import com.datn.ailms.model.dto.response.inventory.GenerateSerialForPOResponse;

public interface IProductScanPOService {
    GenerateSerialForPOResponse generateSerialsForPO(GenerateSerialForPORequest request);
}
