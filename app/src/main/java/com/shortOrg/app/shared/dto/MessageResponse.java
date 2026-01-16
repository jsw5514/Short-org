package com.shortOrg.app.shared.dto;

import lombok.Data;

@Data
public class MessageResponse {
    private final long messageId;
    private final long roomId;
    private final long postId;
    private final String senderId;
    private final String content;
}
