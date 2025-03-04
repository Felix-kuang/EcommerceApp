package com.example.ecommerce.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.ecommerce.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateToken(User user) {

        System.out.println("🔍 Role saat generate token: " + user.getRole());
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", List.of("ROLE_" + user.getRole())) // Simpan dalam array
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 jam
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }
    
    @SuppressWarnings("unchecked")
    public String extractRole(String token) {
        List<String> roles = getClaims(token).get("roles", List.class);
        return roles != null && !roles.isEmpty() ? roles.get(0) : null;
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
