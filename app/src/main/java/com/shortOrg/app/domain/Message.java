package com.shortOrg.app.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post postId;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User senderId;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private User receiverId;

    private String content;
}
