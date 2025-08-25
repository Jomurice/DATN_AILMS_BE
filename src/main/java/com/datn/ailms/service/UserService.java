package com.datn.ailms.service;

import com.datn.ailms.exception.AppException;
import com.datn.ailms.exception.ErrorCode;
import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.mapper.UserMapper;
import com.datn.ailms.model.dto.request.UserRequest;
import com.datn.ailms.model.dto.response.UserResponse;
import com.datn.ailms.model.entity.Role;
import com.datn.ailms.model.entity.User;
import com.datn.ailms.repository.RoleRepository;
import com.datn.ailms.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IUserService {

    UserRepository _userRepository;
    UserMapper _userMapper;
    PasswordEncoder _passwordEncoder;
    RoleRepository _roleRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> results = _userRepository.findAll();
        return _userMapper.toUserResponseList(results);
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = _userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return _userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (_userRepository.existsByUsername(userRequest.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        if (_userRepository.existsByEmail(userRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = _userMapper.toUser(userRequest);
        user.setPassword(_passwordEncoder.encode(userRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String role : userRequest.getRoles()) {
            _roleRepository.findById(role).ifPresent(roles::add);
        }

        user.setRoles(roles);

        User savedUser = _userRepository.save(user);

        return _userMapper.toUserResponse(savedUser);
    }


}
