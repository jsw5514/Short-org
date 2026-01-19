package com.shortOrg.app.shared.dto;

import lombok.Data;

@Data
public class RatingRequest {
    String targetUserId;
    Long score;
    String comment;
}
