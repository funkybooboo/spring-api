package com.funkybooboo.store.services;

import com.funkybooboo.store.config.JwtConfig;
import com.funkybooboo.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    
    public String generateAccessToken(User user) {
        final long tokenExpirationInMillis = jwtConfig.getAccessTokenExpiration();
        return generateToken(user, tokenExpirationInMillis);
    }

    public String generateRefreshToken(User user) {
        final long tokenExpirationInMillis = jwtConfig.getRefreshTokenExpiration();
        return generateToken(user, tokenExpirationInMillis);
    }

    private String generateToken(User user, long tokenExpirationInMillis) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpirationInMillis))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        }
        catch (JwtException ex) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
