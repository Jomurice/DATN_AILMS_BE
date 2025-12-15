package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.filter.OutboundOrderSpec;
import com.datn.ailms.interfaces.order_interface.IOutboundOrderService;
import com.datn.ailms.mapper.OutboundOrderMapper;
import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.OutboundOrderFilter;
import com.datn.ailms.model.dto.request.inventory.ProductConfirmRequestDto;
import com.datn.ailms.model.dto.request.order.CancelRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.enums.OrderStatus;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.order_entites.Customer;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.repositories.orderRepo.CustomerRepository;
import com.datn.ailms.repositories.orderRepo.OutboundOrderItemRepository;
import com.datn.ailms.repositories.orderRepo.OutboundOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundOrderService implements IOutboundOrderService {

    final UserRepository _userRepository;
    final CustomerRepository _customerRepo;
    final ProductRepository _productRepo;
    final ProductDetailRepository _productDetailRepo;
    final OutboundOrderRepository _outboundOrderRepo;
    final OutboundOrderItemRepository _outOrderItemRepo;
    final OutboundOrderMapper _outboundOrderMapper;
    final ProductDetailMapper _productDetailMapper;
    final OutboundOrderItemService _outItemService;

    @Override
    public Page<OutboundOrderResponseDto> search(OutboundOrderFilter f, Pageable pageable) {
       return _outboundOrderRepo
               .findAll(OutboundOrderSpec.filter(f),pageable)
               .map(_outboundOrderMapper::toDto);
    }

    @Override
    public OutboundOrderResponseDto create(OutboundOrderRequestDto request) {
        User user = _userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

//        Customer customer = _customerRepo.findById(request.getCustomer())
//                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));
//
        OutboundOrder outbound = _outboundOrderMapper.toEntity(request);

        outbound.setCreatedBy(user);
        outbound.setStatus(OrderStatus.DRAFT.name());
        outbound.setCreateAt(LocalDateTime.now());
//        outbound.setCustomer(customer);
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
    public OutboundOrderResponseDto getByCode(String code) {
        OutboundOrder outboundOrder = _outboundOrderRepo.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(!outboundOrder.getStatus().equals(OrderStatus.DRAFT.name())){
            throw new AppException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }

        return _outboundOrderMapper.toDto(outboundOrder);
    }

//    @Override
//    public Page<OutboundOrderResponseDto> getAll(Pageable pageable) {
//        Page<OutboundOrder> orders = _outboundOrderRepo.findAll("DRAFT",pageable);
//        return orders.map(_outboundOrderMapper::toDto);
//    }

    @Override
    public OutboundOrderResponseDto update(OutboundOrderRequestDto request, UUID outboundOrderId) {
        OutboundOrder existing = _outboundOrderRepo.findById(outboundOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        Customer customer = _customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));

        existing.setCustomer(customer);
        existing.setStatus(request.getStatus());
        existing.setUpdateAt(LocalDateTime.now());
        _outboundOrderRepo.save(existing);

        return _outboundOrderMapper.toDto(existing);
    }

    @Override
    public void cancelOutbound(UUID orderId, CancelRequestDto request) {
        System.out.println(orderId + "__" + request);

        OutboundOrder order = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        User user = _userRepository.findById(request.getCanceledBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(order.getStatus().equals(OrderStatus.CANCELLED.name()))
            throw new AppException(ErrorCode.ORDER_ALREADY_CANCELED);

        if (order.getStatus().equals(OrderStatus.EXPORT.name()))
            throw new AppException(ErrorCode.ORDER_CANNOT_CANCEL);

        order.setNote(request.getNote());
        order.setCanceledBy(user);
        order.setStatus(OrderStatus.PENDING_CANCEL.name());

        _outboundOrderRepo.save(order);
    }

    @Override
    public void confirmCancel(UUID orderId) {
        OutboundOrder order = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(!order.getStatus().equals(OrderStatus.PENDING_CANCEL.name())){
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        //set lại status các serial
        order.getItems().forEach(item -> _outItemService.returnSerials(item));

        // Xóa liên kết với order
        order.getItems().forEach(item -> item.setOutboundOrder(null));
        order.setStatus(OrderStatus.CANCELLED.name());
        order.setUpdateAt(LocalDateTime.now());
        _outboundOrderRepo.save(order);
    }

    @Override
    public void rejectCancel(UUID orderId) {
        OutboundOrder order = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(!order.getStatus().equals(OrderStatus.PENDING_CANCEL.name()))
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);

        order.setStatus(OrderStatus.CONFIRMED.name());
        _outboundOrderRepo.save(order);
    }

    @Override
    public OutboundOrderResponseDto confirmOrder(OutboundOrderRequestDto request,UUID orderId) {
        OutboundOrder outOrder = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        Customer customer = _customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));

        if(!OrderStatus.DRAFT.name().equals(outOrder.getStatus())){
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        _outItemService.checkAndReserveItems(outOrder.getItems());

        outOrder.setCustomer(customer);
        outOrder.setStatus(OrderStatus.CONFIRMED.name());
        outOrder.setUpdateAt(LocalDateTime.now());
        _outboundOrderRepo.save(outOrder);
        return _outboundOrderMapper.toDto(outOrder);
    }

    @Override
    public OutboundOrderResponseDto confirmExport(UUID orderId, UUID userId) {
        User user = _userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        OutboundOrder outOrder = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(!OrderStatus.CONFIRMED.name().equals(outOrder.getStatus())){
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        boolean allScanned = outOrder.getItems().stream()
                .allMatch(i -> Optional.ofNullable(i.getScannedQuantity()).orElse(0) >= i.getOrderQuantity());


        if(!allScanned){

        }

        List<ProductDetail> details = _productDetailRepo.findByOutboundOrderId(orderId);

        details.forEach(d -> {
            d.setStatus(SerialStatus.OUTBOUND);
            d.setUpdatedAt(LocalDateTime.now());
            d.setOutboundOrderItem(
                    outOrder.getItems().stream()
                            .filter(i -> i.getProduct().getId().equals(d.getProduct().getId()))
                            .findFirst()
                            .orElseThrow(() -> new AppException(ErrorCode.SERIAL_NOT_IN_ORDER))
            );
        });
        _productDetailRepo.saveAll(details);

        outOrder.setStatus(OrderStatus.EXPORT.name());
        outOrder.setUpdateAt(LocalDateTime.now());
        outOrder.setExportedBy(user);

        _outboundOrderRepo.save(outOrder);
        return _outboundOrderMapper.toDto(outOrder);

    }

    @Override
    public List<ProductDetailResponseDto> getByOrderIdAndSKU(UUID orderId, String sku) {
        OutboundOrder outOrder = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        Product product = _productRepo.findBySku(sku)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<ProductDetail> productDetails = _productDetailRepo.findByOrderIdAndSku(orderId,sku);

        return _productDetailMapper.toResponseList(productDetails);
    }

    @Override
    public List<ProductDetailResponseDto> scanned(UUID orderId, ProductConfirmRequestDto request) {
        User user = _userRepository.findById(request.getScannedByUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        OutboundOrder outOrder = _outboundOrderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        ProductDetail productDetail = _productDetailRepo.findBySerialNumber(request.getSerialNumber())
                .orElseThrow(() -> new AppException(ErrorCode.SERIAL_NOT_FOUND));

        //Ktr xem serialNumber trong order k
        OutboundOrderItem item = outOrder.getItems().stream()
                                .filter(i -> i.getProduct().getId().equals(productDetail.getProduct().getId()))
                                .findFirst()
                                .orElseThrow(() -> new AppException(ErrorCode.SERIAL_NOT_IN_ORDER));

        //Ktr xem serialNumber đã quét chưa
        if(productDetail.getStatus() == SerialStatus.SCANNED){
            throw new AppException(ErrorCode.SERIAL_ALREADY_SCANNED);
        }

        int scannedQty = Optional.ofNullable(item.getScannedQuantity()).orElse(0);
        if (scannedQty >= item.getOrderQuantity()) {
            throw new AppException(ErrorCode.QUANTITY_EXCEEDED);
        }

        // cập nhật status sp
        productDetail.setStatus(SerialStatus.SCANNED);
        productDetail.setScannedBy(user);
        productDetail.setOutboundOrderItem(item);
        productDetail.setUpdatedAt(LocalDateTime.now());
        _productDetailRepo.save(productDetail);

        // cập nhật sl đã quét
        item.setScannedQuantity(scannedQty +1);
        _outOrderItemRepo.save(item);

        List<ProductDetail> scannedList = _productDetailRepo.findByOutboundOrderId(orderId);
        return _productDetailMapper.toResponseList(scannedList);
    }

//    @Override
//    public List<OutboundOrderResponseDto> getAllByStatus(String status) {
//        if (status == null || status.trim().isEmpty()) {
//            return getAll();
//        }
//        List<OutboundOrder> orders = _outboundOrderRepo.findByStatus(status);
//        return _outboundOrderMapper.toResponseList(orders);
//    }

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
