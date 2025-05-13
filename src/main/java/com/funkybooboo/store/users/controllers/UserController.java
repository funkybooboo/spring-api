package com.funkybooboo.store.users.controllers;

import com.funkybooboo.store.users.dtos.requests.ChangeUserPasswordRequestDto;
import com.funkybooboo.store.users.dtos.requests.CreateUserRequestDto;
import com.funkybooboo.store.users.dtos.requests.UpdateUserRequestDto;
import com.funkybooboo.store.users.dtos.responses.UserResponseDto;
import com.funkybooboo.store.users.entities.Role;
import com.funkybooboo.store.users.mappers.UserMapper;
import com.funkybooboo.store.users.repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Iterable<UserResponseDto> getAllUsers(
        @RequestParam(name = "sort", required = false, defaultValue = "") String sortBy
    ) {
        if (!Set.of("name", "email").contains(sortBy)) {
            sortBy = "name";
        }
        return userRepository.findAll(Sort.by(sortBy)).stream().map(userMapper::toResponseDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }
    
    @PostMapping
    public ResponseEntity<?> createUser(
        @Valid @RequestBody CreateUserRequestDto userRequestDto,
        UriComponentsBuilder uriBuilder
    ) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("email", "Email is already registered"));
        }
        
        var user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        var userResponseDto = userMapper.toResponseDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponseDto.getId()).toUri();
        
        return ResponseEntity.created(uri).body(userResponseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
        @PathVariable Long id,
        @RequestBody UpdateUserRequestDto userRequestDto
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        userMapper.update(userRequestDto, user);
        userRepository.save(user);
        
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
        @PathVariable Long id
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changeUserPassword(
        @PathVariable Long id,
        @RequestBody ChangeUserPasswordRequestDto userRequestDto
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(userRequestDto.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        user.setPassword(userRequestDto.getNewPassword());
        userRepository.save(user);
        
        return ResponseEntity.noContent().build();
    }
}
