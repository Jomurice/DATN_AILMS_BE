package com.datn.ailms.controllers.checkInventory_controller;

import com.datn.ailms.interfaces.ICheckInventoryService;
import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryItemRequestDTO;
import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryRequestDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryItemResponseDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryResponseDTO;
import com.datn.ailms.model.entities.account_entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/check-inventories")
@RequiredArgsConstructor
public class CheckInventoryController {

    private final ICheckInventoryService checkInventoryService;

    @PostMapping
    public ResponseEntity<CheckInventoryResponseDTO> create(@Valid @RequestBody CheckInventoryRequestDTO dto, Authentication auth) {
        User currentUser = getCurrentUserFromAuth(auth);
        return ResponseEntity.ok(checkInventoryService.create(dto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckInventoryResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody CheckInventoryRequestDTO dto, Authentication auth) {
        User currentUser = getCurrentUserFromAuth(auth);
        return ResponseEntity.ok(checkInventoryService.update(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        checkInventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckInventoryResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(checkInventoryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CheckInventoryResponseDTO>> findAll() {
        return ResponseEntity.ok(checkInventoryService.findAll());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<CheckInventoryResponseDTO>> findByWarehouseLocation(@PathVariable String location) {
        return ResponseEntity.ok(checkInventoryService.findByWarehouseLocation(location));
    }

    @PutMapping("/{checkId}/items")
    public ResponseEntity<CheckInventoryItemResponseDTO> updateItem(@PathVariable UUID checkId, @Valid @RequestBody CheckInventoryItemRequestDTO dto, Authentication auth) {
        User currentUser = getCurrentUserFromAuth(auth);
        return ResponseEntity.ok(checkInventoryService.updateItem(checkId, dto, currentUser));
    }

    private User getCurrentUserFromAuth(Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("Không thể lấy thông tin user hiện tại!");
    }
}