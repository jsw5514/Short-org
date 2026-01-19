package com.shortOrg.app.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id는 무조건 시간순으로 단조증가해야함(로직이 의존하고 있음)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_room_id")
    private MessageRoom messageRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="receiver_id")
    private User receiver;

    private String content;

    public Message(MessageRoom messageRoom, Post post, User sender, User receiver, String content) {
        this.messageRoom = messageRoom;
        this.post = post;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}
