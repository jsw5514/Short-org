package com.shortOrg.app.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "refresh_rotation_cache")
@Getter
public class TokenRotationCache {
    @Id
    @Column(name = "old_token_hash")
    private String oldTokenHash;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "new_access_token_ciphertext")
    private String newAccessTokenCiphertext;

    @Column(name = "new_refresh_token_ciphertext")
    private String newRefreshTokenCiphertext;
    
    @Column(name = "expire_at")
    private Instant expireAt; //캐시 만료시간(10~30초)
}
