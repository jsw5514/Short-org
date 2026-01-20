package com.shortOrg.app.features.auth;

import com.shortOrg.app.features.auth.dto.LoginRequest;
import com.shortOrg.app.features.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        TokenResponse tokens = authService.login(req);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh() {
        TokenResponse tokens = authService.refresh(); //TODO 구현 필요
        return ResponseEntity.ok(tokens);
    }
}
