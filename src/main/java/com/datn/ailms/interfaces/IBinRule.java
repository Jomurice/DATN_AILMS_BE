package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.BinRuleRequestDto;
import com.datn.ailms.model.dto.response.BinRuleResponseDto;
import com.datn.ailms.model.entities.topo_entities.Bin;

import java.util.List;
import java.util.UUID;

public interface IBinRule {
    /**
     * Trả Bin (thực thể) phù hợp cho serial; ném exception nếu không tìm thấy
     */
    Bin findBinForSerial(String serial);

    /**
     * Trả Optional nếu không chắc chắn
     */
    java.util.Optional<Bin> findBinForSerialOptional(String serial);

    // CRUD cho bin rule
    BinRuleResponseDto createRule(BinRuleRequestDto req);
    BinRuleResponseDto updateRule(UUID id, BinRuleRequestDto req);
    void deleteRule(UUID id);
    List<BinRuleResponseDto> listRules();
}
