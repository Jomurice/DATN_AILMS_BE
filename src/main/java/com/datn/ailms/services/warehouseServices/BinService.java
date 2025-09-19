package com.datn.ailms.services.warehouseServices;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IBinService;
import com.datn.ailms.mapper.BinMapper;
import com.datn.ailms.model.dto.request.warehouse_request.CreateBinRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateBinRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.BinResponseDto;
import com.datn.ailms.model.entities.topo_entities.Bin;
import com.datn.ailms.model.entities.topo_entities.Shelf;
import com.datn.ailms.repositories.productRepo.ProductRepository;
import com.datn.ailms.repositories.warehousetopology.BinRepository;
import com.datn.ailms.repositories.warehousetopology.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinService implements IBinService {

    private final BinRepository _binRepository;
    private final BinMapper _binMapper;
    private final ShelfRepository _shelfRepository;
    private final ProductRepository _productRepository;
    @Override
    public List<BinResponseDto> getAllBins() {
        List<Bin> bins = _binRepository.findAll();
        return _binMapper.toBinResponseDtoList(bins);
    }

    @Override
    public List<BinResponseDto> findAllByShelfId(UUID shelfId) {
        if (!_shelfRepository.existsById(shelfId)) {
            throw new AppException(ErrorCode.SHELF_NOT_EXISTED);
        }
        List<Bin> bins = _binRepository.findAllByShelfIdNativeQuery(shelfId);
        return _binMapper.toBinResponseDtoList(bins);
    }

    @Override
    public BinResponseDto getBinById(UUID binId) {
        Bin bin = _binRepository.findById(binId).orElseThrow(
                () -> new AppException(ErrorCode.BIN_NOT_EXISTED)
        );
        return _binMapper.toBinResponseDto(bin);
    }

    @Override
    public BinResponseDto createBin(CreateBinRequestDto request) {
        Shelf shelf = _shelfRepository.findById(request.getShelfId()).orElseThrow(
                () -> new AppException(ErrorCode.SHELF_NOT_EXISTED)
        );



        Bin bin = _binMapper.toBin(request);
        bin.setShelf(shelf);
        bin.setPreferredProductId(request.getPreferredProductId());
        bin.setCreatedAt(LocalDateTime.now());
        bin.setUpdatedAt(LocalDateTime.now());

        return _binMapper.toBinResponseDto(_binRepository.save(bin));
    }

    @Override
    public BinResponseDto updateBin(UUID binId, UpdateBinRequestDto request) {
        Bin bin = _binRepository.findById(binId).orElseThrow(
                () -> new AppException(ErrorCode.BIN_NOT_EXISTED)
        );

        bin.setCode(request.getCode());
        bin.setName(request.getName());
        if (request.getShelfId() != null) {
            Shelf shelf = _shelfRepository.findById(request.getShelfId()).orElseThrow(
                    () -> new AppException(ErrorCode.SHELF_NOT_EXISTED)
            );
            bin.setShelf(shelf);
        }
        bin.setPreferredProductId(request.getPreferredProductId());
        bin.setUpdatedAt(LocalDateTime.now());

        return _binMapper.toBinResponseDto(_binRepository.save(bin));
    }
}
