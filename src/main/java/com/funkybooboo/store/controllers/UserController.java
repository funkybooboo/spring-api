package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.request.RegisterUserRequestDto;
import com.funkybooboo.store.dtos.request.UpdateUserRequestDto;
import com.funkybooboo.store.dtos.response.UserResponseDto;
import com.funkybooboo.store.mappers.UserMapper;
import com.funkybooboo.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
    public ResponseEntity<UserResponseDto> createUser(
        @RequestBody RegisterUserRequestDto userRequestDto,
        UriComponentsBuilder uriBuilder
    ) {
        var user = userMapper.toEntity(userRequestDto);
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
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
