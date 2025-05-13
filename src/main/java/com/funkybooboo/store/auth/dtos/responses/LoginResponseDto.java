package com.funkybooboo.store.auth.dtos.responses;

import com.funkybooboo.store.auth.services.Jwt;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private Jwt accessToken;
    private Jwt refreshToken;
}
