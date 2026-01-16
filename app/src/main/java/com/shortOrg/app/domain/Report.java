package com.shortOrg.app.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="reporter_id")
    private Post reporterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="target_id")
    private User targetId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="post_id")
    private User postId;

    private String description;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
