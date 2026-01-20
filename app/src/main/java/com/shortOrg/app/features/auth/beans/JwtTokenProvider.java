package com.shortOrg.app.features.auth.beans;

import com.shortOrg.app.features.auth.dto.JwtResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtTokenProvider {
    private final SecretKey key;

    public JwtTokenProvider(String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    JwtResult createToken(String subject, Map<String, Object> claims, long ttlMs) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttlMs);
        
        Claims finalClaims = Jwts.claims()
                .subject(subject)
                .add(claims)
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(exp)
                .build();

        String token = Jwts.builder()
                .claims(finalClaims)
                .signWith(key)
                .compact();
        
        return new JwtResult(
                token,
                null,
                finalClaims
        );
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}