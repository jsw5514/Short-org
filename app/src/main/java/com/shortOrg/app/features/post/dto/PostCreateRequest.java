package com.shortOrg.app.features.post.dto;

import com.shortOrg.app.features.user.dto.JoinMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCreateRequest {
    String category;
    String title;
    String content;
    LocalDateTime meetingTime;
    Double longitude;
    Double latitude;
    JoinMode joinMode;
}
