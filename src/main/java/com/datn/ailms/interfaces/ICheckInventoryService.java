package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryItemRequestDTO;
import com.datn.ailms.model.dto.request.checkInventory.CheckInventoryRequestDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryItemResponseDTO;
import com.datn.ailms.model.dto.response.checkInventory.CheckInventoryResponseDTO;
import com.datn.ailms.model.entities.account_entities.User;

import java.util.List;
import java.util.UUID;

public interface ICheckInventoryService {
    CheckInventoryResponseDTO create(CheckInventoryRequestDTO dto, User currentUser);
    CheckInventoryResponseDTO update(UUID id, CheckInventoryRequestDTO dto, User currentUser);
    void delete(UUID id);
    CheckInventoryResponseDTO findById(UUID id);
    List<CheckInventoryResponseDTO> findAll();
    List<CheckInventoryResponseDTO> findByWarehouseLocation(String location);
    CheckInventoryItemResponseDTO updateItem(UUID checkId, CheckInventoryItemRequestDTO dto, User currentUser);
}