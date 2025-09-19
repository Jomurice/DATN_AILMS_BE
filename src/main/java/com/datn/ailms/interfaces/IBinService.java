package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.warehouse_request.CreateBinRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.CreateShelfRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateBinRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.UpdateShelfRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.BinResponseDto;
import com.datn.ailms.model.dto.response.warehouse_response.ShelfResponseDto;
import com.datn.ailms.model.entities.topo_entities.Bin;

import java.util.List;
import java.util.UUID;

public interface IBinService {
    List<BinResponseDto> getAllBins();
    List<BinResponseDto> findAllByShelfId(UUID shelfId);
    BinResponseDto getBinById(UUID binId);
    BinResponseDto createBin(CreateBinRequestDto request);
    BinResponseDto updateBin(UUID binId, UpdateBinRequestDto request);

}
