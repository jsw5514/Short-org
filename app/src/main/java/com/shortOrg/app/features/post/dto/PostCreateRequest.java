package com.shortOrg.app.features.post.dto;

import com.shortOrg.app.shared.enumerate.JoinMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCreateRequest {
    private String category;
    private String title;
    private String content;
    private LocalDateTime meetingTime;
    private String locationName;
    private Double longitude;
    private Double latitude;
    private Integer capacity;
    private JoinMode joinMode;
}
