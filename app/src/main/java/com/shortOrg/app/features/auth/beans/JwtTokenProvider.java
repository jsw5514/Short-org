package com.shortOrg.app.features.auth.beans;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtTokenProvider {

    private final Key key;
    private final long accessTtlMs;
    private final long refreshTtlMs;

    public JwtTokenProvider(String secret, long accessTtlMs, long refreshTtlMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes()); // secret은 충분히 길어야 함(권장 256bit+)
        this.accessTtlMs = accessTtlMs;
        this.refreshTtlMs = refreshTtlMs;
    }

    public String createAccessToken(String username, Map<String, Object> claims) {
        return createToken(username, claims, accessTtlMs);
    }

    public String createRefreshToken(String username) {
        return createToken(username, Map.of("typ", "refresh"), refreshTtlMs);
    }

    private String createToken(String subject, Map<String, Object> claims, long ttlMs) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttlMs);

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
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

    public String getUsername(String token) {
        return parse(token).getPayload().getSubject();
    }
}

