package com.shortOrg.app.domain;

import com.shortOrg.app.shared.dto.JoinMode;
import com.shortOrg.app.shared.dto.PostStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id")
    private User writerId;

    private String category;
    private String title;
    private String content;

    @Column(name = "date_time")
    private LocalDateTime meetingTime;

    private Double longitude;
    private Double latitude;

    @Enumerated(EnumType.STRING)
    private PostStatus state;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_mode")
    private JoinMode joinMode;

    @Column(name = "last_modified")
    private LocalDateTime lastModified = LocalDateTime.now();
}