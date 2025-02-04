package com.example.Demo_Product.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "YourSuperSecretKeyForJWTSigning12345678901234567890";
    private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    private static final long JWT_EXPIRATION = 3600000; // 1 hour

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();  // Return the email
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token has expired", e);
        } catch (Exception e) {
            throw new RuntimeException("JWT Decoding Error: " + e.getMessage(), e);
        }
    }
}
