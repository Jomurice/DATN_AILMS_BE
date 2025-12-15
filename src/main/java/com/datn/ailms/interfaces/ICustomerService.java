package com.datn.ailms.interfaces;


import com.datn.ailms.model.dto.request.order.CustomerRequestDto;
import com.datn.ailms.model.dto.response.order.CustomerResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ICustomerService {
    Page<CustomerResponseDto> getAll(int page, int size, String search, Boolean status);
    CustomerResponseDto getById(UUID customerId);
    CustomerResponseDto create(CustomerRequestDto request);
    CustomerResponseDto update(UUID customerId, CustomerRequestDto request);
    CustomerResponseDto toggleCustomerStatus(UUID customerId);
    void delete(UUID customerId);

}
