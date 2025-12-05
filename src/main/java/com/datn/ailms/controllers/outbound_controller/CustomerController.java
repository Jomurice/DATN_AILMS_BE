package com.datn.ailms.controllers.outbound_controller;

import com.datn.ailms.model.dto.request.order.CustomerRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.order.CustomerResponseDto;
import com.datn.ailms.repositories.orderRepo.CustomerRepository;
import com.datn.ailms.services.orderService.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

    final CustomerService _customerService;

    @GetMapping
    public ApiResp<Page<CustomerResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean status
    ){
        var result = _customerService.getAll(page,size,search,status);
        return ApiResp.<Page<CustomerResponseDto>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{customerId}")
    public ApiResp<CustomerResponseDto> getById(@PathVariable UUID customerId){
        var result = _customerService.getById(customerId);
        return ApiResp.<CustomerResponseDto>builder()
                .result(result)
                .build();
    }

    @PostMapping
    public ApiResp<CustomerResponseDto> create(@RequestBody CustomerRequestDto request){
        var result = _customerService.create(request);
        return ApiResp.<CustomerResponseDto>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{customerId}")
    public ApiResp<CustomerResponseDto> update(@PathVariable UUID customerId,
                                               @RequestBody CustomerRequestDto request){
        var result = _customerService.update(customerId, request);
        return ApiResp.<CustomerResponseDto>builder()
                .result(result)
                .build();
    }

    @PatchMapping("/{customerId}/active")
    public ApiResp<CustomerResponseDto> toggleStatus(@PathVariable UUID customerId){
        var result = _customerService.toggleCustomerStatus(customerId);
        return ApiResp.<CustomerResponseDto>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{customerId}")
    public ApiResp<Void> delete(@PathVariable UUID customerId){
        _customerService.delete(customerId);
        return ApiResp.<Void>builder()
                .message("Customer deleted successfully")
                .build();
    }



}
