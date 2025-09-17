package com.datn.ailms.services.WarehouseServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IShelfService;
import com.datn.ailms.mapper.ShelfMapper;
import com.datn.ailms.model.dto.request.warehouse_request.CreateShelfRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateShelfRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.ShelfResponseDto;
import com.datn.ailms.model.entities.topo_entities.Shelf;
import com.datn.ailms.repositories.warehousetopology.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShelfService implements IShelfService {

    private final ShelfRepository shelfRepository;
    private final ShelfMapper shelfMapper;

    @Override
    public List<ShelfResponseDto> getAllShelves() {
        List<Shelf> shelves = shelfRepository.findAll();
        return shelfMapper.toShelfResponseDtoList(shelves);
    }

    @Override
    public ShelfResponseDto getShelfById(UUID shelfId) {
        Shelf shelf = shelfRepository.findById(shelfId).orElseThrow(
                () -> new AppException(ErrorCode.SHELF_NOT_EXISTED)
        );
        return shelfMapper.toShelfResponseDto(shelf);
    }

    @Override
    public ShelfResponseDto createShelf(CreateShelfRequestDto request) {
        Shelf shelf = shelfMapper.toShelf(request);
        shelf.setCreatedAt(LocalDateTime.now());
        shelf.setUpdatedAt(LocalDateTime.now());

        return shelfMapper.toShelfResponseDto(shelfRepository.save(shelf));
    }


    @Override
    public ShelfResponseDto updateShelf(UUID shelfId, UpdateShelfRequestDto request) {
        Shelf shelf = shelfRepository.findById(shelfId).orElseThrow(
                () -> new AppException(ErrorCode.SHELF_NOT_EXISTED)
        );
        shelf.setCode(request.getCode());
        shelf.setName(request.getName());
        shelf.setUpdatedAt(LocalDateTime.now());

        return shelfMapper.toShelfResponseDto(shelfRepository.save(shelf));
    }
}