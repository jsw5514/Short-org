package com.shortOrg.app.repository;

import com.shortOrg.app.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
