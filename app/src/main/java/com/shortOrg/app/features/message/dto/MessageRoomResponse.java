package com.shortOrg.app.features.message.dto;

import lombok.Data;

@Data
public class MessageRoomResponse {
    private final long roomId;
    private final String opponentId;
    private final long postId;
    private final String lastMessage;
    private final String title;
    private long notReadCount;
}
