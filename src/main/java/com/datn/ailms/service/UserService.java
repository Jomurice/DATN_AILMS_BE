package com.datn.ailms.service;

import com.datn.ailms.exception.AppException;
import com.datn.ailms.exception.ErrorCode;
import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.mapper.UserMapper;
import com.datn.ailms.model.dto.response.UserResponse;
import com.datn.ailms.model.entity.User;
import com.datn.ailms.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IUserService {


    UserRepository _userRepository;

    UserMapper _userMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        return _userMapper.toResponseList(_userRepository.findAll());
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = _userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return _userMapper.toResponse(user);
    }
}
