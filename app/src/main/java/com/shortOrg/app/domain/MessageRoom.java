package com.shortOrg.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "message_room",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_message_room_post_users",
                columnNames = {"post_id", "user1_id", "user2_id"}
        )
)
public class MessageRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
//-----------------최근 메시지 프리뷰 최적화용-------------------------------
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "last_message_id", nullable = false)
    private Message lastMessage;

    public MessageRoom(User user1, User user2, Post post) {
        int compareResult = user1.getId().compareTo(user2.getId());
        if (compareResult < 0) {
            this.user1 = user1;
            this.user2 = user2;
        } else if (compareResult > 0) {
            this.user1 = user2;
            this.user2 = user1;
        } else 
            throw new IllegalArgumentException("자기 자신과의 채팅방은 만들 수 없음");
        
        this.post = post;
    }

    public Optional<User> getOpponent(String userId){
        if(user1.getId().equals(userId)) 
            return Optional.of(user1);
        else if (user2.getId().equals(userId))
            return  Optional.of(user2);
        return Optional.empty();
    }
}
