package com.funkybooboo.store.users.services;

import com.funkybooboo.store.users.exceptions.UserNotFoundException;
import com.funkybooboo.store.users.dtos.requests.ChangeUserPasswordRequestDto;
import com.funkybooboo.store.users.dtos.requests.RegisterUserRequestDto;
import com.funkybooboo.store.users.dtos.requests.UpdateUserRequestDto;
import com.funkybooboo.store.users.dtos.responses.UserResponseDto;
import com.funkybooboo.store.users.entities.Role;
import com.funkybooboo.store.users.exceptions.DuplicateUserException;
import com.funkybooboo.store.users.mappers.UserMapper;
import com.funkybooboo.store.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Iterable<UserResponseDto> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";
        
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto getUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto registerUser(RegisterUserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateUserException();
        }

        var user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    public UserResponseDto updateUser(Long userId, UpdateUserRequestDto requestDto) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userMapper.update(requestDto, user);
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public void changePassword(Long userId, ChangeUserPasswordRequestDto requestDto) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Password does not match");
        }

        user.setPassword(requestDto.getNewPassword());
        userRepository.save(user);
    }
}
