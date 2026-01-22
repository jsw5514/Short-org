package com.shortOrg.app.features.auth;

import com.shortOrg.app.domain.Token;
import com.shortOrg.app.domain.TokenRotationCache;
import com.shortOrg.app.features.auth.beans.JwtManager;
import com.shortOrg.app.features.auth.beans.TokenEncoder;
import com.shortOrg.app.features.auth.dto.JwtResult;
import com.shortOrg.app.features.auth.dto.LoginRequest;
import com.shortOrg.app.features.auth.dto.TokenResponse;
import com.shortOrg.app.repository.TokenHistoryRepository;
import com.shortOrg.app.repository.TokenRepository;
import com.shortOrg.app.repository.TokenRotationCacheRepository;
import com.shortOrg.app.shared.enumerate.TokenState;
import com.shortOrg.app.features.auth.dto.TokenValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtManager jwt;
    private final TokenRepository tokenRepository;
    private final TokenEncoder tokenEncoder;
    private final TokenHistoryRepository tokenHistoryRepository;
    private final TokenRotationCacheRepository tokenRotationCacheRepository;

    public TokenResponse login(LoginRequest req) {
        //id, pw 검증
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.id(), req.password())
        );

        //토큰 발급
        JwtResult access = jwt.createAccessToken(auth.getName());
        JwtResult refresh = jwt.createRefreshToken(auth.getName());
        
        tokenRepository.save(
                new Token(
                        auth.getName(), 
                        tokenEncoder.encode(refresh.token()),
                        refresh.claims().getIssuedAt().toInstant()
                )
        );
        
        //발급된 토큰 반환
        return new TokenResponse(access.token(), refresh.token());
    }

    public TokenResponse refresh(String userId) {
        //토큰 발급
        JwtResult access = jwt.createAccessToken(userId);
        JwtResult refresh = jwt.createRefreshToken(userId);

        Token dbtoken = tokenRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("리프레시 토큰 정보가 존재하지 않음"));
        dbtoken.setTokenHash(tokenEncoder.encode(refresh.token()));
        dbtoken.setUpdatedAt(refresh.claims().getIssuedAt().toInstant());
        tokenRepository.save(dbtoken);

        return new TokenResponse(access.token(), refresh.token());
    }
    
    public TokenValidationResult validateRefreshToken(String username, String token) {
        //리프레시 토큰 유효성 검증
        log.info("리프레시 토큰 유효성 검사 시작. 유저명: {}", username);

        if (isValidToken(username, token)) { //유효한 토큰
            log.info("유효한 토큰");
            return new TokenValidationResult(TokenState.VALID_ACTIVE, null);
        } 
        
        //유효한 토큰이 아님, 해시를 이용하여 만료 토큰 재사용 여부 확인
        String tokenHash = tokenEncoder.encode(token);

        if (isReusedToken(username, tokenHash)) { //재사용 탐지되지 않음, 그냥 발급기록이 없는 토큰
            log.error("토큰 발급 기록을 찾을 수 없음");
            return new TokenValidationResult(TokenState.INVALID_UNKNOWN, null);
        } 
        
        //재사용 탐지됨, 짧은 시간(10~30초)내의 재사용은 네트워크 문제로 인한 재시도로 인정
        return findRecentRotation(tokenHash)
                .map(cache -> {
                    log.warn("토큰이 재사용되었지만 최근에 갱신된 기록 발견. 네트워크 문제로 인한 재시도로 간주함.");
                    return new TokenValidationResult(TokenState.VALID_REPLAY, cache.getOldTokenHash());
                })
                .orElseGet(() -> {
                    log.error("토큰 재사용 감지됨");
                    return new TokenValidationResult(TokenState.INVALID_REUSED, null);
                });
    }
    
//토큰 검증을 위한 private 메소드---------------------------------------------------------------------------------------------------------------------
    private boolean isValidToken(String username, String token) {
        return tokenRepository.findById(username)
                .filter(tokenEntity -> tokenEncoder.matches(token, tokenEntity.getTokenHash()))
                .isPresent();
    }

    private boolean isReusedToken(String username, String tokenHash) {
        return !tokenHistoryRepository.existsByUser_IdAndTokenHash(username, tokenHash);
    }

    private Optional<TokenRotationCache> findRecentRotation(String tokenHash) {
        return tokenRotationCacheRepository.findByOldTokenHashAndExpireAtAfter(tokenHash, Instant.now());
    }
}