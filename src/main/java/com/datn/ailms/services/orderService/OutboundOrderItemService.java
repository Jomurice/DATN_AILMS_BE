package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IStockRepository;
import com.datn.ailms.interfaces.order_interface.IOutboundOrderItemService;
import com.datn.ailms.mapper.OutboundOrderItemMapper;
import com.datn.ailms.model.dto.request.order.OutboundOrderItemRequestDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.repositories.orderRepo.OutboundOrderItemRepository;
import com.datn.ailms.repositories.orderRepo.OutboundOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.services.StockService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundOrderItemService implements IOutboundOrderItemService {

    final ProductRepository _productRepository;
    final ProductDetailRepository _productDetailRepository;
    final IStockRepository _stockRepository;
    final OutboundOrderRepository _outOutboundOrderRepository;
    final OutboundOrderItemRepository _itemRepo;
    final OutboundOrderItemMapper _itemMapper;
    final StockService _stockService;

    @Override
    public List<OutboundOrderItemResponseDto> addItem(List<OutboundOrderItemRequestDto> request, UUID outboundOrder) {

            OutboundOrder order = _outOutboundOrderRepository.findById(outboundOrder)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            List<OutboundOrderItem> itemsToSave = request.stream()
                    .map(req -> {
                        Product product = _productRepository.findById(req.getProductId())
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                        int available = _stockService.getAvailableQuantity(req.getProductId());
                        if (req.getOrderQuantity() > available) throw new AppException(ErrorCode.STOCK_NOT_ENOUGH);

                        return OutboundOrderItem.builder()
                                .orderQuantity(req.getOrderQuantity())
                                .outboundOrder(order)
                                .product(product)
                                .build();

                    })
                    .collect(Collectors.toList());
            List<OutboundOrderItem> savedItems = _itemRepo.saveAll(itemsToSave);
        return savedItems.stream()
                .map(_itemMapper::toDto)
                .toList();
    }

    @Override
    public List<OutboundOrderItemResponseDto> update(OutboundOrderItemRequestDto request, UUID id) {
        return null;
    }

    @Override
    public void removeItem(UUID itemId) {

    }
}
