package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.requests.LoginRequestDto;
import com.funkybooboo.store.dtos.responses.JwtResponseDto;
import com.funkybooboo.store.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
        @Valid @RequestBody LoginRequestDto requestDto
    ) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            requestDto.getEmail(),
            requestDto.getPassword()
        ));
        
        var token = jwtService.generateToken(requestDto.getEmail());
        
        return ResponseEntity.ok(new JwtResponseDto(token));
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
