package com.ecommerce.onlineshopsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
//import javax.xml.crypto.Data;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration:3600000}")
    private long expirationMs;


    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] secretBytes = secretString.getBytes(StandardCharsets.UTF_8);

        if (secretBytes.length < 32) {
            throw new IllegalArgumentException(
                    "jwt.secret must contain at least 32 UTF-8 bytes for HS256"
            );
        }

        this.key = Keys.hmacShaKeyFor(secretBytes);
    }



    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return  extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
      return extractAllClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token,  String username) {
        try{
            Claims claims = extractAllClaims(token);
            String extractedUsername = claims.getSubject();
            Date  expiration = claims.getExpiration();

            return extractedUsername.equals(username) && expiration.after(new Date());

        }catch (Exception e) {
            return false;
        }
    }



    }
