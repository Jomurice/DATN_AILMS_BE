//package com.datn.ailms.controllers.topocontrollers;
//
//
//import com.datn.ailms.model.dto.request.BinRuleRequestDto;
//import com.datn.ailms.model.dto.response.ApiResp;
//import com.datn.ailms.model.dto.response.BinRuleResponseDto;
//import com.datn.ailms.services.topoServices.BinRuleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/bin-rules")
//@RequiredArgsConstructor
//public class BinRuleController {
//
//    private final BinRuleService _binRuleService;
//
//    @PostMapping
//    ApiResp<BinRuleResponseDto> createRule(@RequestBody BinRuleRequestDto req) {
//        var result =  _binRuleService.createRule(req);
//        return ApiResp.<BinRuleResponseDto>builder().result(result).build();
//    }
//
//    @PutMapping("/{id}")
//     ApiResp<BinRuleResponseDto> updateRule(
//            @PathVariable UUID id,
//            @RequestBody BinRuleRequestDto req
//    ) {
//        var result =  _binRuleService.updateRule(id,req);
//        return ApiResp.<BinRuleResponseDto>builder().result(result).build();
//    }
//
//    @DeleteMapping("/{id}")
//    ApiResp<Void> deleteRule(@PathVariable UUID id) {
//        _binRuleService.deleteRule(id);
//        return ApiResp.<Void>builder().build();
//    }
//
//    @GetMapping
//     ApiResp<List<BinRuleResponseDto>> listRules() {
//        var result = _binRuleService.listRules();
//        return ApiResp.<List<BinRuleResponseDto>>builder().result(result).build();
//    }
//}
