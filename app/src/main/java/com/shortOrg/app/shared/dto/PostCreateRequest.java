package com.shortOrg.app.shared.dto;

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
