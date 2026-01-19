package com.shortOrg.app.domain.embeddedId;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RoomReadStateId implements Serializable {
    @Column(name = "room_id")
    Long roomId;
    @Column(name = "user_id")
    String userId;
}
