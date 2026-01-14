package com.shortOrg.app.features.auth;

import com.shortOrg.app.features.auth.beans.JwtTokenProvider;
import com.shortOrg.app.features.auth.dto.LoginRequest;
import com.shortOrg.app.features.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwt;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.id(), req.password())
        );
        
        String access = jwt.createAccessToken(auth.getName());
        String refresh = jwt.createRefreshToken(auth.getName());

        return ResponseEntity.ok(new TokenResponse(access, refresh));
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
}
