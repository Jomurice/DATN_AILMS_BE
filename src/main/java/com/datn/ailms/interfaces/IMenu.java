package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.menu.CreateMenuRequestDto;
import com.datn.ailms.model.dto.request.menu.UpdateMenuRequestDto;
import com.datn.ailms.model.dto.response.menu.MenuResponseDto;

import java.util.List;

public interface IMenu {
    List<MenuResponseDto> getAll();
    MenuResponseDto create(CreateMenuRequestDto requestDto);
    MenuResponseDto update(UpdateMenuRequestDto requestDto);
    List<MenuResponseDto> getMenuTree();
}
