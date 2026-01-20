package com.shortOrg.app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String userId;
    
    private String token;
    
    @Column(name = "issued_at")
    private Instant issuedAt;
    
    @Column(name = "expires_at")
    private Instant expiresAt;
}
