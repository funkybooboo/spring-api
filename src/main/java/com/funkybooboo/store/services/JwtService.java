package com.funkybooboo.store.services;

import com.funkybooboo.store.config.JwtConfig;
import com.funkybooboo.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    
    public Jwt generateAccessToken(User user) {
        return generateJwt(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        return generateJwt(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateJwt(User user, long tokenExpirationInMillis) {
        var claims = Jwts.claims()
            .subject(user.getId().toString())
            .add("email", user.getEmail())
            .add("name", user.getName())
            .add("role", user.getRole())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpirationInMillis))
            .build();
        
        return new Jwt(claims, jwtConfig.getSecretKey());
    }
    
    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException ex) {
            return null;
        }
    }
    
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
