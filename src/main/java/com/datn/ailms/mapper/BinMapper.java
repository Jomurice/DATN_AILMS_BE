package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.warehouse_request.CreateBinRequestDto;
import com.datn.ailms.model.dto.request.warehouse_request.CreateShelfRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.BinResponseDto;
import com.datn.ailms.model.dto.response.warehouse_response.ShelfResponseDto;
import com.datn.ailms.model.entities.topo_entities.Bin;
import com.datn.ailms.model.entities.topo_entities.Shelf;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BinMapper {
    Bin toBin(CreateBinRequestDto request);
    BinResponseDto toBinResponseDto(Bin bin);
    List<BinResponseDto> toBinResponseDtoList(List<Bin> Bins);
}

