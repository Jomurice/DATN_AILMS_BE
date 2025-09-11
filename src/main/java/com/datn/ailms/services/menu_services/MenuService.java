package com.datn.ailms.services.menu_services;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IMenu;
import com.datn.ailms.mapper.MenuMapper;
import com.datn.ailms.model.dto.request.menu.CreateMenuRequestDto;
import com.datn.ailms.model.dto.request.menu.UpdateMenuRequestDto;
import com.datn.ailms.model.dto.response.menu.MenuResponseDto;
import com.datn.ailms.model.entities.other_entities.Menu;
import com.datn.ailms.repositories.menu_repo.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuService implements IMenu {
    final MenuRepository _menuRepository;
    final MenuMapper _menuMapper;

    @Override
    public List<MenuResponseDto> getAll() {
        List<Menu>  menus = _menuRepository.findAll();
        return _menuMapper.toMenuResponseList(menus);
    }

    @Override
    @Transactional
    public MenuResponseDto create(CreateMenuRequestDto requestDto) {
        if(_menuRepository.existsByTitle(requestDto.getTitle())){
            throw new AppException(ErrorCode.MENU_EXISTED);
        }
        Menu menu = _menuMapper.toEntity(requestDto);
        if(requestDto.getParentId() != null){
            Menu parentMenu = _menuRepository.findById(requestDto.getParentId()).orElseThrow(() -> new AppException(ErrorCode.PARENT_NOT_FOUND));
            menu.setParent(parentMenu);
        }

        Menu menuSave = _menuRepository.save(menu);
        return _menuMapper.toMenuResponse(menuSave);

    }
    @Override
    @Transactional
    public MenuResponseDto update(UpdateMenuRequestDto requestDto) {
        Menu menu = _menuRepository.findById(requestDto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.MENU_NOT_FOUND));

        // MapStruct update từ DTO vào entity
        _menuMapper.updateEntityFromDto(requestDto, menu);

        // Xử lý parentId riêng (vì mapper ignore parent)
        if (requestDto.getParentId() != null) {
            Menu menuParent = _menuRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.PARENT_NOT_FOUND));
            menu.setParent(menuParent);
        } else {
            menu.setParent(null);
        }

        Menu saved = _menuRepository.save(menu);
        return _menuMapper.toMenuResponse(saved);
    }


    @Override
    public List<MenuResponseDto> getMenuTree() {
        // Lấy tất cả menu cha (parentId = null)
        List<Menu> rootMenus = _menuRepository.findByParentIsNull();

        // Map sang DTO (đệ quy children đã có trong MenuMapper)
        return _menuMapper.toMenuResponseList(rootMenus);
    }
}
