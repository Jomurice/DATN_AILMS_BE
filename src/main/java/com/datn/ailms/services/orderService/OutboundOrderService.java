package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.order_interface.IOutboundOrderService;
import com.datn.ailms.mapper.OutboundOrderMapper;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.enums.OrderStatus;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.repositories.orderRepo.OutboundOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        outbound.setStatus(OrderStatus.DRAFT.name());
        outbound.setCreateAt(LocalDateTime.now());
        outbound.setCustomer(request.getCustomer());
        outbound.setCode(generateOrderCode());

        outbound = _outboundOrderRepo.save(outbound);
        return _outboundOrderMapper.toDto(outbound);
    }

    @Override
    public OutboundOrderResponseDto getById(UUID id) {
        OutboundOrder outboundOrder = _outboundOrderRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return _outboundOrderMapper.toDto(outboundOrder);
    }

    @Override
    public List<OutboundOrderResponseDto> getAll() {
        List<OutboundOrder> orders = _outboundOrderRepo.findAll("DRAFT");
        return _outboundOrderMapper.toResponseList(orders);

    }

    @Override
    public OutboundOrderResponseDto update(OutboundOrderRequestDto request, UUID outboundOrderId) {
        OutboundOrder existing = _outboundOrderRepo.findById(outboundOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        existing.setCustomer(request.getCustomer());
        existing.setStatus(request.getStatus());
        existing.setUpdateAt(LocalDateTime.now());
        _outboundOrderRepo.save(existing);

        return _outboundOrderMapper.toDto(existing);
    }

    @Override
    public OutboundOrderResponseDto confirmOrder(OutboundOrderRequestDto request,UUID orderId) {
        OutboundOrder outOrder = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(!OrderStatus.DRAFT.name().equals(outOrder.getStatus())){
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        _outItemService.checkAndReserveItems(outOrder.getItems());

        outOrder.setCustomer(request.getCustomer());
        outOrder.setStatus(OrderStatus.CONFIRMED.name());
        outOrder.setUpdateAt(LocalDateTime.now());
        _outboundOrderRepo.save(outOrder);
        return _outboundOrderMapper.toDto(outOrder);
    }

    @Override
    public List<OutboundOrderResponseDto> getAllByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return getAll();
        }
        List<OutboundOrder> orders = _outboundOrderRepo.findByStatus(status);
        return _outboundOrderMapper.toResponseList(orders);
    }

    @Override
    public List<OutboundOrderResponseDto> getAllByProductId(UUID productId) {
        List<OutboundOrder> orders = _outboundOrderRepo.findByProductId(productId);
        return _outboundOrderMapper.toResponseList(orders);
    }

    @Scheduled(cron = "0 0 * * * *") // chạy mỗi giờ
    public void deleteExpiredDraftOrders() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        List<OutboundOrder> expiredOrders = _outboundOrderRepo.findByStatusAndCreateAtBefore("DRAFT", cutoffTime);

        if (expiredOrders.isEmpty()) return;

        _outboundOrderRepo.deleteAll(expiredOrders);
    }

    private String generateOrderCode() {
        long count = _outboundOrderRepo.count() + 1;
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("OUT_HD-%s-%04d", datePart, count);
    }



}
