package com.datn.ailms.mapper;

import com.datn.ailms.model.dto.response.UserResponse;
import com.datn.ailms.model.entity.Role;
import com.datn.ailms.model.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);

}
