package com.shortOrg.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "refresh_token_history",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_refresh_token_history_user_id_token_hash",
                columnNames = {"user_id", "token_hash"}
        ),
        indexes = {
                @Index(name = "idx_refresh_token_history_user_event_at", columnList = "user_id, event_at")
        }
)
@Getter
public class TokenHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "token_hash", nullable = false)
    private String tokenHash;
    
    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    @Column(name = "event_at")
    private Instant eventAt;
}
