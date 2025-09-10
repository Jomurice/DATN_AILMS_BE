package com.datn.ailms.controllers.menu_controller;

import com.datn.ailms.model.dto.request.menu.CreateMenuRequestDto;
import com.datn.ailms.model.dto.request.menu.UpdateMenuRequestDto;
import com.datn.ailms.model.dto.response.ApiResp;
import com.datn.ailms.model.dto.response.menu.MenuResponseDto;
import com.datn.ailms.model.entities.other_entities.Menu;
import com.datn.ailms.services.menu_services.MenuService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuController {
    final MenuService _menuService;
    @PostMapping
    ApiResp<MenuResponseDto> create(@RequestBody CreateMenuRequestDto createMenuRequestDto) {
         var result = _menuService.create(createMenuRequestDto);
         return ApiResp.<MenuResponseDto>builder().result(result).build();
    }

    @GetMapping
    ApiResp<List<MenuResponseDto>> getAll() {
        var result = _menuService.getAll();
        return ApiResp.<List<MenuResponseDto>>builder().result(result).build();
    }

    @GetMapping("/tree")
    ApiResp<List<MenuResponseDto>> getTree() {
        var result = _menuService.getMenuTree();
        return ApiResp.<List<MenuResponseDto>>builder().result(result).build();
    }

    @PutMapping("/{id}")
    ApiResp<MenuResponseDto> update(@PathVariable UUID id,@RequestBody UpdateMenuRequestDto requestDto){
        requestDto.setId(id);

        var result = _menuService.update(requestDto);
        return ApiResp.<MenuResponseDto>builder().result(result).build();
    }


}
