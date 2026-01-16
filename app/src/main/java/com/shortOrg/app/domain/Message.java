package com.shortOrg.app.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_room_id")
    private MessageRoom messageRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="post_id")
    private Post postId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="sender_id")
    private User senderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="receiver_id")
    private User receiverId;

    private String content;
}
