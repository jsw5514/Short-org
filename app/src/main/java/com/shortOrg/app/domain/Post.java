package com.shortOrg.app.domain;

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
    private LocalDateTime mettingTime;

    private Double longitude;
    private Double latitude;
    private String state;

    @Column(name = "join_mode")
    private String joinMode;

    @Column(name = "last_modified")
    private LocalDateTime lastModified = LocalDateTime.now();
}