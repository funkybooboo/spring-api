package com.funkybooboo.store.users.controllers;

import com.funkybooboo.store.users.dtos.requests.ChangeUserPasswordRequestDto;
import com.funkybooboo.store.users.dtos.requests.RegisterUserRequestDto;
import com.funkybooboo.store.users.dtos.requests.UpdateUserRequestDto;
import com.funkybooboo.store.users.dtos.responses.UserResponseDto;
import com.funkybooboo.store.users.exceptions.DuplicateUserException;
import com.funkybooboo.store.users.exceptions.UserNotFoundException;
import com.funkybooboo.store.users.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    
    @GetMapping
    public Iterable<UserResponseDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequestDto requestDto,
            UriComponentsBuilder uriBuilder) {
        var userDto = userService.registerUser(requestDto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable Long id,
            @RequestBody ChangeUserPasswordRequestDto requestDto) {
        userService.changePassword(id, requestDto);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUser() {
        return ResponseEntity.badRequest().body(
                Map.of("email", "Email is already registered.")
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
