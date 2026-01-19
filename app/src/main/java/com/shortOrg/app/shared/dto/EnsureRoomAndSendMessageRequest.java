package com.shortOrg.app.shared.dto;

import lombok.Data;

@Data
public class EnsureRoomAndSendMessageRequest {
    Long postId;
    String receiverId;
    String content;
}
