package com.shortOrg.app.domain;

import jakarta.persistence.*;

@Entity
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post postId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;

    private String state;
}
