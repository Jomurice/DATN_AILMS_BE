package com.datn.ailms.services.warehouseServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IShelfService;
import com.datn.ailms.mapper.ShelfMapper;
import com.datn.ailms.model.dto.request.warehouse_request.CreateShelfRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateShelfRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.ShelfResponseDto;
import com.datn.ailms.model.entities.topo_entities.Shelf;
import com.datn.ailms.repositories.warehousetopology.AisleRepository;
import com.datn.ailms.repositories.warehousetopology.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShelfService implements IShelfService {

    private final ShelfRepository _shelfRepository;
    private final ShelfMapper _shelfMapper;
    private final AisleRepository _aisleRepo;

    @Override
    public List<ShelfResponseDto> getAllShelves() {
        List<Shelf> shelves = _shelfRepository.findAll();
        return _shelfMapper.toShelfResponseDtoList(shelves);
    }

    @Override
    public List<ShelfResponseDto> findAllByAisleIdNativeQuery(UUID shelfId) {
        List<Shelf> shelves = _shelfRepository.findAllByAisleIdNativeQuery(shelfId);
        return _shelfMapper.toShelfResponseDtoList(shelves);
    }

    @Override
    public ShelfResponseDto getShelfById(UUID shelfId) {
        Shelf shelf = _shelfRepository.findById(shelfId).orElseThrow(
                () -> new AppException(ErrorCode.SHELF_NOT_EXISTED)
        );
        return _shelfMapper.toShelfResponseDto(shelf);
    }

    @Override
    public ShelfResponseDto createShelf(CreateShelfRequestDto request) {
        Shelf shelf = _shelfMapper.toShelf(request);

        var aisle = _aisleRepo.findById(request.getAisleId()).orElseThrow(() -> new AppException(ErrorCode.AISLE_NOT_EXISTED));
        shelf.setAisle(aisle);
        shelf.setCreatedAt(LocalDateTime.now());
        shelf.setUpdatedAt(LocalDateTime.now());

        return _shelfMapper.toShelfResponseDto(_shelfRepository.save(shelf));
    }


    @Override
    public ShelfResponseDto updateShelf(UUID shelfId, UpdateShelfRequestDto request) {
        Shelf shelf = _shelfRepository.findById(shelfId).orElseThrow(
                () -> new AppException(ErrorCode.SHELF_NOT_EXISTED)
        );
        shelf.setCode(request.getCode());
        shelf.setName(request.getName());
        shelf.setUpdatedAt(LocalDateTime.now());

        return _shelfMapper.toShelfResponseDto(_shelfRepository.save(shelf));
    }
}