package com.datn.ailms.services.orderService;


import com.datn.ailms.interfaces.order_interface.IPurchaseOrderService;
import com.datn.ailms.mapper.PurchaseOrderMapper;
import com.datn.ailms.model.dto.request.order.PurchaseOrderRequestDto;
import com.datn.ailms.model.dto.response.order.PurchaseOrderResponseDto;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import com.datn.ailms.model.entities.product_entities.Product;
import com.datn.ailms.repositories.orderRepo.PurchaseOrderRepository;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderService implements IPurchaseOrderService {

    final PurchaseOrderRepository _orderRepo;
    final PurchaseOrderMapper _orderMapper;
    final ProductRepository _productRepository;
    final UserRepository _userRepository;
    @Override
    public PurchaseOrderResponseDto create(PurchaseOrderRequestDto request) {
        PurchaseOrder order = _orderMapper.toEntity(request);
        // Nếu entity đã dùng @GeneratedValue thì bỏ dòng dưới
        User user = _userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getCreatedBy()));
        order.setCreatedBy(user);

        // Gắn quan hệ 2 chiều: mỗi item phải biết order cha
        if (order.getItems() != null) {
            order.getItems().forEach(item -> {
                item.setPurchaseOrder(order);

                // Lấy productId ra
                UUID productId = item.getProduct().getId();

                // Fetch product từ DB
                Product product = _productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

                // Gán lại product đầy đủ
                item.setProduct(product);
            });
        }
        PurchaseOrder savedOrder = _orderRepo.save(order);
        return _orderMapper.toDto(savedOrder);
    }

    @Override
    public PurchaseOrderResponseDto getById(UUID id) {
        PurchaseOrder order = _orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
        return _orderMapper.toDto(order);
    }

    @Override
    public List<PurchaseOrderResponseDto> getAll() {
        return _orderMapper.toResponseList(_orderRepo.findAll());
    }

    @Override
    public PurchaseOrderResponseDto update(UUID id, PurchaseOrderRequestDto request) {
        PurchaseOrder existing = _orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
        // MapStruct có thể hỗ trợ update nếu bạn khai báo @MappingTarget
        existing.setCode(request.getCode());
        existing.setSupplier(request.getSupplier());
        existing.setStatus(request.getStatus());
        existing.setCreatedAt(request.getCreatedAt());
        // Items update có thể phức tạp → cần xử lý thêm
        return _orderMapper.toDto(_orderRepo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        _orderRepo.deleteById(id);
    }
}
