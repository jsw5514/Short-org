package com.shortOrg.app.shared.dto;

import lombok.Data;

@Data
public class MessageRoomResponse {
    int roomId;
    String opponentId;
    int postId;
}
