package com.funkybooboo.store.controllers;

import com.funkybooboo.store.config.JwtConfig;
import com.funkybooboo.store.dtos.requests.LoginRequestDto;
import com.funkybooboo.store.dtos.responses.JwtResponseDto;
import com.funkybooboo.store.dtos.responses.UserResponseDto;
import com.funkybooboo.store.mappers.UserMapper;
import com.funkybooboo.store.repositories.UserRepository;
import com.funkybooboo.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
        @Valid @RequestBody LoginRequestDto requestDto,
        HttpServletResponse response
    ) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            requestDto.getEmail(),
            requestDto.getPassword()
        ));
        
        var user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(); // will never not exist as the filters block emails that don't exist in the db
        
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);
        
        return ResponseEntity.ok(new JwtResponseDto(accessToken));
    }
    
    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader) {
        var token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        var userResponseDto = userMapper.toResponseDto(user);
        
        return ResponseEntity.ok(userResponseDto);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
