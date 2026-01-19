package com.shortOrg.app.domain;

import com.shortOrg.app.domain.embeddedId.RoomReadStateId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "room_read_state",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_read_state_room_user",
                columnNames = {"room_id", "user_id"}
        )
)
public class RoomReadState {
    @EmbeddedId
    private RoomReadStateId id;

    @Setter
    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private MessageRoom messageRoom;

    @Setter
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "last_read_message_id", nullable = false)
    private Message lastReadMessage;
    
    public RoomReadState() {
        id = new RoomReadStateId();
    }
}
