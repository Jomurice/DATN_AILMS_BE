package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.order.CustomerRequestDto;
import com.datn.ailms.model.dto.response.order.CustomerResponseDto;
import com.datn.ailms.model.entities.order_entites.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toCustomer(CustomerRequestDto request);
    CustomerResponseDto toCustomerResponse(Customer customer);
    List<CustomerResponseDto> customerResponseList(List<Customer> customers);

}
