package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.UserRequestDto;
import com.datn.ailms.model.dto.response.UserResponseDto;

import com.datn.ailms.model.entities.account_entities.User;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    User toUser(UserRequestDto userRequest);

    UserResponseDto toUserResponse(User user);

    List<UserResponseDto> toUserResponseList(List<User> users);

}
