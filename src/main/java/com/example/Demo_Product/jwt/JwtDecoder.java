package com.example.Demo_Product.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

public class JwtDecoder {

    public static Claims decodeToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey("YourSuperSecretKeyForJWTSigning12345678901234567890")
                    .parseClaimsJws(token)  // Parses the JWT token and automatically handles URL-safe Base64
                    .getBody();
        } catch (JwtException e) {
            System.out.println("Error parsing token: " + e.getMessage());
        }
        return null;
    }
}
