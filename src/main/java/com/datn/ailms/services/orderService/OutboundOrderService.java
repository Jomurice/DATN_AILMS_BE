package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.order_interface.IOutboundOrderService;
import com.datn.ailms.mapper.OutboundOrderMapper;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.repositories.orderRepo.OutboundOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundOrderService implements IOutboundOrderService {

    final UserRepository _userRepository;
    final ProductRepository _productRepository;
    final OutboundOrderRepository _outboundOrderRepo;
    final OutboundOrderMapper _outboundOrderMapper;
    final OutboundOrderItemService _outItemService;

    @Override
    public OutboundOrderResponseDto create(OutboundOrderRequestDto request) {
        User user = _userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        OutboundOrder outbound = _outboundOrderMapper.toEntity(request);

        outbound.setCreatedBy(user);
        outbound.setStatus("processing");
        outbound.setCreateAt(LocalDateTime.now());
        outbound.setCustomer(request.getCustomer());

        outbound = _outboundOrderRepo.save(outbound);

        List<OutboundOrderItemResponseDto> addedItems = null;
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            addedItems = _outItemService.addItem(request.getItems(), outbound.getId());
        }

        // Map lại OutboundOrder -> Response DTO
        OutboundOrderResponseDto response = _outboundOrderMapper.toDto(outbound);
        response.setItems(addedItems);

        return response;
    }

    @Override
    public OutboundOrderResponseDto getById(UUID id) {
        OutboundOrder outboundOrder = _outboundOrderRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return _outboundOrderMapper.toDto(outboundOrder);
    }

    @Override
    public List<OutboundOrderResponseDto> getAll() {
        return _outboundOrderMapper.toResponseList(_outboundOrderRepo.findAll());
    }

    @Override
    public OutboundOrderResponseDto update(OutboundOrderRequestDto request, UUID id) {
//        OutboundOrder existing = _outboundOrderRepo.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        existing.setCustomer(request.getCustomer());
//        existing.setStatus(request.getStatus());
//        existing.setUpdateAt(new Date());
//
//        if(request.getItems() != null && !request.getItems().isEmpty()){
//            _outItemService.removeItem(id);
//            _outItemService.addItem(request.getItems(),id);
//        }
//
//        existing = _outboundOrderRepo.save(existing);
//        OutboundOrderResponseDto responseDto = _outboundOrderMapper.toDto(existing);
//        responseDto.setItems(_outItemService.get);
//
        return null;
    }
}
