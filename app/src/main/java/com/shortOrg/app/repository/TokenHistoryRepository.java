package com.shortOrg.app.repository;

import com.shortOrg.app.domain.TokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenHistoryRepository extends JpaRepository<TokenHistory, Long> {
    boolean existsByUser_IdAndTokenHash(String username, String encode);
}
