package com.shortOrg.app.features.auth.beans;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class TokenEncoder {
    private final PasswordEncoder passwordEncoder;

    //입력 문자열을 sha256 해시 알고리즘을 적용한 32바이트 고정길이 문자열로 변환
    public static String sha256Hex(String s) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(s.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) { //getInstance의 체크 예외 처리용(SHA-256에서는 발생할 일이 거의 없음)
            throw new IllegalStateException(e);
        }
    }
    
    //토큰 문자열을 SHA-256으로 정규화하여 길이를 맞춘뒤 bcrypt로 암호화
    public String encode(String token) {
        return passwordEncoder.encode(sha256Hex(token));
    }
    
    //인코딩 전후 문자열의 일치판정
    public boolean matches(String token, String hash) {
        return passwordEncoder.matches(sha256Hex(token), hash);
    }
}
