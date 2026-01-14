package com.shortOrg.app.features.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCreateRequest {
    String userId;
    String category;
    String title;
    String content;
    LocalDateTime meetingTime;
    Double longitude;
    Double latitude;
    Enum<JoinMode> joinMode;
}
