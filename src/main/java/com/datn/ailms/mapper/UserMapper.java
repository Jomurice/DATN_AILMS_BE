package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.request.UserRequest;
import com.datn.ailms.model.dto.response.UserResponse;

import com.datn.ailms.model.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    User toUser(UserRequest userRequest);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

}
