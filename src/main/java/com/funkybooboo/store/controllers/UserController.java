package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.requests.ChangeUserPasswordRequestDto;
import com.funkybooboo.store.dtos.requests.CreateUserRequestDto;
import com.funkybooboo.store.dtos.requests.UpdateUserRequestDto;
import com.funkybooboo.store.dtos.responses.UserResponseDto;
import com.funkybooboo.store.mappers.UserMapper;
import com.funkybooboo.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
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
        @Valid @RequestBody CreateUserRequestDto userRequestDto,
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
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException exception
    ) {
        var errors = new HashMap<String, String>();
        
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        
        return ResponseEntity.badRequest().body(errors);
    }
}
