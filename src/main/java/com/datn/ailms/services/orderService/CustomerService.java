package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.ICustomerService;
import com.datn.ailms.mapper.CustomerMapper;
import com.datn.ailms.model.dto.request.order.CustomerRequestDto;
import com.datn.ailms.model.dto.response.order.CustomerResponseDto;
import com.datn.ailms.model.entities.order_entites.Customer;
import com.datn.ailms.repositories.orderRepo.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class CustomerService implements ICustomerService {

    final CustomerRepository _customerRepo;
    final CustomerMapper _customerMapper;

    @Override
    public Page<CustomerResponseDto> getAll(int page, int size, String search, Boolean status) {
        if(search == null) search = "";
        Pageable pageable = PageRequest.of(page,size, Sort.by("firstName").ascending());

        return _customerRepo.searchCustomer(search, status, pageable)
                .map(_customerMapper::toCustomerResponse);
    }

    @Override
    public CustomerResponseDto getById(UUID customerId) {
        Customer customer = getCustomer(customerId);

        return _customerMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponseDto create(CustomerRequestDto request) {
        validateDuplicateContact(request.getEmail(), request.getPhone(), null);

        Customer customer = _customerMapper.toCustomer(request);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setStatus(true);

        _customerRepo.save(customer);
        return _customerMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponseDto update(UUID customerId, CustomerRequestDto request) {
        Customer customer = getCustomer(customerId);

        validateDuplicateContact(request.getEmail(), request.getPhone(), customerId);

        customer.setLastName(request.getLastName());
        customer.setFirstName(request.getFirstName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setDob(request.getDob());
        customer.setAddress(request.getAddress());
        customer.setGender(request.isGender());
        customer.setUpdatedAt(LocalDateTime.now());

        _customerRepo.save(customer);
        return _customerMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponseDto toggleCustomerStatus(UUID customerId) {
        Customer customer = getCustomer(customerId);

        customer.setStatus(!customer.isStatus());
        customer.setUpdatedAt(LocalDateTime.now());

        _customerRepo.save(customer);
        return _customerMapper.toCustomerResponse(customer);
    }

    @Override
    public void delete(UUID customerId) {
        if (!_customerRepo.existsById(customerId)) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_EXISTED);
        }
        _customerRepo.deleteById(customerId);
    }



    private Customer getCustomer(UUID id) {
        return _customerRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));
    }

    private void validateDuplicateContact(String email, String phone, UUID customerId) {
        if (_customerRepo.existsByEmailAndIdNot(email, customerId)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (_customerRepo.existsByPhoneAndIdNot(phone, customerId)) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
    }
}
