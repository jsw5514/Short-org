package com.shortOrg.app.features.rating.dto;

import lombok.Data;

@Data
public class RatingRequest {
    String targetUserId;
    Long score;
    String comment;
}
