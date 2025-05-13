package com.funkybooboo.store.auth.controllers;

import com.funkybooboo.store.auth.dtos.responses.JwtResponseDto;
import com.funkybooboo.store.auth.services.AuthService;
import com.funkybooboo.store.auth.dtos.requests.LoginRequestDto;
import com.funkybooboo.store.auth.configs.JwtConfig;
import com.funkybooboo.store.users.dtos.responses.UserResponseDto;
import com.funkybooboo.store.users.mappers.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDto login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletResponse response) {

        var loginResult = authService.login(request);
        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponseDto(loginResult.getAccessToken().toString());
    }

    @PostMapping("/refresh")
    public JwtResponseDto refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponseDto(accessToken.toString());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        var user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toResponseDto(user);

        return ResponseEntity.ok(userDto);
    }
}
