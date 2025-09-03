package com.datn.ailms.services;

import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.repositories.userRepo.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.mapper.UserMapper;
import com.datn.ailms.model.dto.request.UserRequestDto;
import com.datn.ailms.model.dto.response.UserResponseDto;
import com.datn.ailms.model.entities.Role;
import com.datn.ailms.model.entities.User;
import com.datn.ailms.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public List<UserResponseDto> getAllUsers() {
        List<User> users = _userRepository.findAll();
        return _userMapper.toUserResponseList(users);
    }

    @Override
    public List<UserResponseDto> getUsersByNameContainingIgnoreCase(String name) {
        List<User> users = _userRepository.findByNameContainingIgnoreCase(name);
        return _userMapper.toUserResponseList(users);
    }

    @Override
    public UserResponseDto getUserById(String id) {
        User user = _userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        System.out.println(user.toString());
        return _userMapper.toUserResponse(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequest) {
        if (_userRepository.existsByUsername(userRequest.getUsername())) {
            throw new AppException(com.datn.ailms.exceptions.ErrorCode.USERNAME_EXISTED);
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

    public UserResponseDto updateUser(String id, UserRequestDto userRequest) {
        User user = _userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        user.setUsername(userRequest.getUsername());
        user.setName(userRequest.getName());
        user.setPhone(userRequest.getPhone());
        user.setEmail(userRequest.getEmail());
        user.setGender(userRequest.isGender());
        user.setDob(userRequest.getDob());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            user.setPassword(_passwordEncoder.encode(userRequest.getPassword()));
        }
        user.setEmail(userRequest.getEmail());

        if (userRequest.getRoles() != null && !userRequest.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String role : userRequest.getRoles()) {
                _roleRepository.findById(role).ifPresent(roles::add);
            }
            user.setRoles(roles);
        }

        User updateUser = _userRepository.save(user);
        return _userMapper.toUserResponse(updateUser);

    }
}

