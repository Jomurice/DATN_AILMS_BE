package com.datn.ailms.services;

import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.interfaces.IUserService;
import com.datn.ailms.model.dto.request.ChangePasswordRequestDto;
import com.datn.ailms.repositories.userRepo.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.mapper.UserMapper;
import com.datn.ailms.model.dto.request.UserRequestDto;
import com.datn.ailms.model.dto.response.UserResponseDto;
import com.datn.ailms.model.entities.account_entities.Role;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
        List<User> users = _userRepository.findAllWithRoles();
        return _userMapper.toUserResponseList(users);
    }

    public User getUserEntityByEmail(String email) {
        return _userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user = _userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return _userMapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponseDto> searchUsers(String name,
                                             String role,
                                             Boolean status,
                                             Boolean gender,
                                             Pageable pageable) {

        Page<User> users = _userRepository.searchUsers(name, role, status, gender, pageable);

        return users.map(_userMapper::toUserResponse);
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

    @Transactional
    public void updateUserPassword(String userId, String newPassword) {
        User user = _userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        user.setPassword(_passwordEncoder.encode(newPassword));
        _userRepository.save(user);
    }


    @Override
    public void changePassword(String username, ChangePasswordRequestDto request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        User user = _userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!_passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        user.setPassword(_passwordEncoder.encode(request.getNewPassword()));
        _userRepository.save(user);
    }

    @Override
    public UserResponseDto activeAccount(String id) {
        User user = _userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        user.setStatus(true);
        User savedUser = _userRepository.save(user);

        return _userMapper.toUserResponse(savedUser);
    }
    @Override
    public UserResponseDto blockedAccount(String id) {
        User user = _userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        user.setStatus(false);
        User savedUser = _userRepository.save(user);

        return _userMapper.toUserResponse(savedUser);
    }


}

