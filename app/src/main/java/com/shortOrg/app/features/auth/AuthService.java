package com.shortOrg.app.features.auth;

import com.shortOrg.app.domain.Token;
import com.shortOrg.app.features.auth.beans.JwtManager;
import com.shortOrg.app.features.auth.beans.TokenEncoder;
import com.shortOrg.app.features.auth.dto.JwtResult;
import com.shortOrg.app.features.auth.dto.LoginRequest;
import com.shortOrg.app.features.auth.dto.TokenResponse;
import com.shortOrg.app.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtManager jwt;
    private final TokenRepository tokenRepository;
    private final TokenEncoder tokenEncoder;

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

    public boolean validateRefreshToken(String username, String token) {
        Optional<Token> dbToken = tokenRepository.findById(username);
        return dbToken
                .filter(tokenEntity -> tokenEncoder.matches(token, tokenEntity.getTokenHash()))
                .isPresent();
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
}