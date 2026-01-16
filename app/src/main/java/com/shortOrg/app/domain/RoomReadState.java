package com.shortOrg.app.domain;

import com.shortOrg.app.domain.embeddedId.RoomReadStateId;
import jakarta.persistence.*;

@Entity
@Table(name = "room_read_state")
public class RoomReadState {
    @EmbeddedId
    private RoomReadStateId id;
    
    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private MessageRoom messageRoom;
    
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "last_read_message_id", nullable = false)
    private Message lastReadMessage;
}
