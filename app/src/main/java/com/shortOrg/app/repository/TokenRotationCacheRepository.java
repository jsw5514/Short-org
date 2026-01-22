package com.shortOrg.app.repository;

import com.shortOrg.app.domain.TokenRotationCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface TokenRotationCacheRepository extends JpaRepository<TokenRotationCache, String> {
    Optional<TokenRotationCache> findByOldTokenHashAndExpireAtAfter(String oldTokenHash, Instant expireAt);
}
