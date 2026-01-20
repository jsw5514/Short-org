package com.shortOrg.app.features.auth.beans;

import com.shortOrg.app.features.auth.dto.JwtResult;

import java.util.Map;

public class JwtManager {

    private final long accessTtlMs;
    private final long refreshTtlMs;
    private final JwtTokenProvider jwtTokenProvider;
    public JwtManager(JwtTokenProvider jwtTokenProvider, long accessTtlMs, long refreshTtlMs) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.accessTtlMs = accessTtlMs;
        this.refreshTtlMs = refreshTtlMs;
    }

    public JwtResult createAccessToken(String username) {
        return jwtTokenProvider.createToken(username, Map.of("typ", "access"), accessTtlMs);
    }

    public JwtResult createRefreshToken(String username) {
        return jwtTokenProvider.createToken(username, Map.of("typ", "refresh"), refreshTtlMs);
    }

    public boolean isValid(String token) {
        return jwtTokenProvider.isValid(token);
    }

    public String getUsername(String token) {
        return jwtTokenProvider.parse(token).getPayload().getSubject();
    }
    
    public String getTokenType(String token) {
        return (String) jwtTokenProvider.parse(token).getPayload().get("typ");
    }
    
    public String getJti(String token) {
        return (String) jwtTokenProvider.parse(token).getPayload().get("jti");
    }
}

