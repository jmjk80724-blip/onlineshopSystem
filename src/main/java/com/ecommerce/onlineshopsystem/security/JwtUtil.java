package com.ecommerce.onlineshopsystem.security;

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

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role",  String.class);
    }

    public boolean validateToken(String token,  String username) {
        try{
           return  extractUsername(token).equals(username) && !isTokenExpired(token);

        }catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }
    }
