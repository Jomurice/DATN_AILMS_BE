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
        List<User> results = _userRepository.findAll();
        return _userMapper.toUserResponseList(results);
    }

    @Override
    public List<UserResponseDto> getUsersByUsername(String username) {
        return null;
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
    public UserResponseDto createUser(UserRequestDto request) {
        if (_userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(com.datn.ailms.exceptions.ErrorCode.USERNAME_EXISTED);
        }

        if (_userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = _userMapper.toUser(request);
        user.setPassword(_passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String role : request.getRoles()) {
            _roleRepository.findById(role).ifPresent(roles::add);
        }

        user.setRoles(roles);

        User savedUser = _userRepository.save(user);

        return _userMapper.toUserResponse(savedUser);
    }

    public UserResponseDto updateUser(String id, UserRequestDto request) {
        User user = _userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setGender(request.isGender());
        user.setDob(request.getDob());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(_passwordEncoder.encode(request.getPassword()));
        }
        user.setEmail(request.getEmail());


        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String role : request.getRoles()) {
                _roleRepository.findById(role).ifPresent(roles::add);
            }
            user.setRoles(roles);
        }

        User updateUser = _userRepository.save(user);
        return _userMapper.toUserResponse(updateUser);


    }
}

