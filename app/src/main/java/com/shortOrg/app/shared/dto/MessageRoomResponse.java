package com.shortOrg.app.shared.dto;

import lombok.Data;

@Data
public class MessageRoomResponse {
    private final long roomId;
    private final String opponentId;
    private final long postId;
    private long notReadCount;
    private String lastMessage;
}
