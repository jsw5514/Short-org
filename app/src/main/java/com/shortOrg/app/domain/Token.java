package com.shortOrg.app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "refresh_token",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_token_token_hash",
                columnNames = {"token_hash"}
        )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String userId;
    
    @Setter
    @Column(name = "token_hash")
    private String tokenHash;
    
    @Setter
    @Column(name = "updated_at")
    private Instant updatedAt;
}
