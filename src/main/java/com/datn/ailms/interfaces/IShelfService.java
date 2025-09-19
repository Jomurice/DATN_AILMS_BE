package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.warehouse_request.CreateShelfRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateShelfRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.ShelfResponseDto;
import com.datn.ailms.model.entities.topo_entities.Shelf;

import java.util.List;
import java.util.UUID;

public interface IShelfService {
    List<ShelfResponseDto> getAllShelves();
    List<ShelfResponseDto> findAllByAisleIdNativeQuery(UUID shelfId);
    ShelfResponseDto getShelfById(UUID shelfId);
    ShelfResponseDto createShelf(CreateShelfRequestDto request);
    ShelfResponseDto updateShelf(UUID shelfId, UpdateShelfRequestDto request);
}
