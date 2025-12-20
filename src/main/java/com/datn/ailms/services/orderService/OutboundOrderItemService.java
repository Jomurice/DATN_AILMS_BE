package com.datn.ailms.services.orderService;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IStockRepository;
import com.datn.ailms.interfaces.order_interface.IOutboundOrderItemService;
import com.datn.ailms.mapper.OutboundOrderItemMapper;
import com.datn.ailms.mapper.ProductDetailMapper;
import com.datn.ailms.model.dto.request.order.OutboundOrderItemRequestDto;
import com.datn.ailms.model.dto.request.order.OutboundOrderRequestDto;
import com.datn.ailms.model.dto.response.ProductDetailSerialDto;
import com.datn.ailms.model.dto.response.inventory.ProductDetailResponseDto;
import com.datn.ailms.model.dto.response.order.OutboundOrderItemResponseDto;
import com.datn.ailms.model.entities.enums.OrderStatus;
import com.datn.ailms.model.entities.enums.SerialStatus;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.order_entites.OutboundOrderItem;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.repositories.orderRepo.OutboundOrderItemRepository;
import com.datn.ailms.repositories.orderRepo.OutboundOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductDetailRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.services.StockService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutboundOrderItemService implements IOutboundOrderItemService {

    final ProductRepository _productRepository;
    final ProductDetailRepository _productDetailRepository;
    final ProductDetailMapper _productDetailMapper;
    final OutboundOrderRepository _outboundOrderRepository;
    final OutboundOrderItemRepository _itemRepo;
    final OutboundOrderItemMapper _itemMapper;
    final StockService _stockService;

    @Override
    public List<OutboundOrderItemResponseDto> addItem(OutboundOrderRequestDto requests, UUID outboundOrderId) {

        OutboundOrder order = _outboundOrderRepository.findById(outboundOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // findAll item theo outboundOrderId
        Map<UUID,OutboundOrderItem> existingItemMap = _itemRepo.findByOutboundOrderId(outboundOrderId)
                .stream()
                .collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));

        // lọc trùng id
        Set<UUID> newProductIds = requests.getItems().stream()
                .map(OutboundOrderItemRequestDto::getProductId)
                .collect(Collectors.toSet());

        List<ProductDetail> serialsToSave = new ArrayList<>();
        List<OutboundOrderItem> itemsToSave = new ArrayList<>();

        // xóa các item k còn trong req
        removeItem(newProductIds,existingItemMap);

        Warehouse warehouse = order.getWarehouse();

        // Thêm hoặc update item
        for(OutboundOrderItemRequestDto req : requests.getItems()){
            Product product = _productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));


            OutboundOrderItem item = existingItemMap.getOrDefault(product.getId(),
                    OutboundOrderItem.builder()
                            .orderQuantity(req.getOrderQuantity())
                            .outboundOrder(order)
                            .product(product)
                            .build()
                    );

            item.setOrderQuantity(req.getOrderQuantity());
            itemsToSave.add(item);

            List<ProductDetail> serials = _productDetailRepository
                    .findTopNByProductAndWarehouse(
                            product.getId(),
                            warehouse.getId(),
                            PageRequest.of(0, req.getOrderQuantity())
                    );
            serialsToSave.addAll(serials);

        }
        _itemRepo.saveAll(itemsToSave);
        if (order.getStatus().equals(OrderStatus.CONFIRMED)) {
            _productDetailRepository.saveAll(serialsToSave);
        }

        return itemsToSave
                .stream()
                .map(_itemMapper::toDto)
                .toList();
    }

    @Override
    public void removeItem(Set<UUID> productIds, Map<UUID, OutboundOrderItem> existingItemMap) {
        List<OutboundOrderItem> itemsToDelete = existingItemMap.values().stream()
                .filter(i -> !productIds.contains(i.getProduct().getId()))
                .toList();
        for(OutboundOrderItem item: itemsToDelete){
            List<ProductDetail> serials = _productDetailRepository.findByOutboundOrderItemId(item.getId());
            serials.forEach(pd -> pd.setStatus(SerialStatus.IN_WAREHOUSE));
            _productDetailRepository.saveAll(serials);
        }
        _itemRepo.deleteAll(itemsToDelete);
    }

    @Override
    public void returnSerials(OutboundOrderItem item) {
        if (item.getProductDetails() == null || item.getProductDetails().isEmpty()) return;

        List<ProductDetail> serials = item.getProductDetails();
        for(ProductDetail pd : serials){
            pd.setStatus(SerialStatus.IN_WAREHOUSE);
            pd.setOutboundOrderItem(null);
            pd.setUpdatedAt(LocalDateTime.now());
            System.out.println(pd.getSerialNumber());
        }
        _productDetailRepository.saveAll(serials);
    }

    // chỉ xử lý khi status order đã Confirmed
//    @Override
//    public OutboundOrderItemResponseDto addSingleItem(OutboundOrderItemRequestDto request, UUID orderId) {
//        OutboundOrder outOrder = _outboundOrderRepository.findById(orderId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        if(!outOrder.getStatus().equals(OrderStatus.CONFIRMED)){
//            throw new AppException(ErrorCode.ORDER_ALREADY_COMPLETED);
//        }
//
//        Product product = _productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
//
//        // check tồn kho
//        long available = _stockService.getAvailableQuantity(product.getId());
//        if (request.getOrderQuantity() > available)
//            throw new AppException(ErrorCode.STOCK_NOT_ENOUGH);
//
//        OutboundOrderItem item = OutboundOrderItem.builder()
//                .outboundOrder(outOrder)
//                .product(product)
//                .orderQuantity(request.getOrderQuantity())
//                .build();
//
//        _itemRepo.save(item);
//
//        // Lấy serials và cập nhật trạng thái
//        List<ProductDetail> serials = _productDetailRepository
//                .findByProductIdAndStatus(product.getId(), SerialStatus.IN_WAREHOUSE)
//                .stream()
//                .limit(request.getOrderQuantity()) // Chỉ lấy số lượng SP = vs số lượng SP trong req
//                .toList();
//
//        final OutboundOrderItem finalItem = item;
//        assignSerialsToItem(serials, finalItem);
//
//        _productDetailRepository.saveAll(serials);
//        return _itemMapper.toDto(finalItem);
//
//    }


    @Override
    public OutboundOrderItemResponseDto update(OutboundOrderItemRequestDto request, UUID outboundOrderId, UUID warehouseId) {
        OutboundOrderItem item = _itemRepo.findByOutboundOrderIdAndProductId(outboundOrderId, request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

        item.setOrderQuantity(request.getOrderQuantity());
        _itemRepo.save(item);

        if (item.getOutboundOrder().getStatus().equals(OrderStatus.CONFIRMED)) {
            // Reset serial cũ về kho
            List<ProductDetail> oldSerials = _productDetailRepository.findByOutboundOrderItemId(item.getId());
            oldSerials.forEach(pd -> pd.setStatus(SerialStatus.IN_WAREHOUSE));
            _productDetailRepository.saveAll(oldSerials);

            // Lấy lại serials mới
            List<ProductDetail> newSerials = _productDetailRepository
                    .findAvailableForExport(request.getProductId(),warehouseId)
                    .stream()
                    .limit(request.getOrderQuantity())
                    .toList();

            newSerials.forEach(pd -> {
                pd.setStatus(SerialStatus.OUTBOUND);
                pd.setOutboundOrderItem(item);
            });
            _productDetailRepository.saveAll(newSerials);
        }

        return _itemMapper.toDto(item);
    }

    @Override
    public void deleteItem(UUID productId, UUID outOrderId) {
        OutboundOrderItem item = _itemRepo.findByOutboundOrderIdAndProductId(outOrderId,productId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

        List<ProductDetail> serials = _productDetailRepository.findByOutboundOrderItemId(item.getId());
        serials.forEach(pd -> pd.setStatus(SerialStatus.IN_WAREHOUSE));
        _productDetailRepository.saveAll(serials);
        _itemRepo.delete(item);

    }


    @Override
    public List<OutboundOrderItemResponseDto> getItemsByOrderId(UUID outboundOrderId) {
        return _itemRepo.findByOutboundOrderId(outboundOrderId)
                .stream()
                .map(_itemMapper::toDto)
                .toList();
    }

    private List<ProductDetail> assignSerialsToItem(List<ProductDetail> serials, OutboundOrderItem item) {
        serials.forEach(pd -> {
            pd.setStatus(SerialStatus.RESERVED);
            pd.setOutboundOrderItem(item);
        });
        return serials;
    }

    public void checkAndReserveItems(List<OutboundOrderItem> items, UUID warehouseId){
        for(OutboundOrderItem item : items){
            Product product = item.getProduct();

            long available = _stockService.getAvailableQuantity(product.getId(), warehouseId);
            if (item.getOrderQuantity() > available)
                throw new AppException(ErrorCode.STOCK_NOT_ENOUGH);


            List<ProductDetail> serials = _productDetailRepository
                    .findAvailableForExport(product.getId(), warehouseId)
                    .stream()
                    .limit(item.getOrderQuantity()) // Chỉ lấy số lượng SP = vs số lượng SP trong req
                    .toList();

            final OutboundOrderItem finalItem = item;
            assignSerialsToItem(serials, finalItem);
        }
    }


}
