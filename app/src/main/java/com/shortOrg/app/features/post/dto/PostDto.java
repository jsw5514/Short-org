package com.shortOrg.app.features.post.dto;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.shared.enumerate.JoinMode;
import com.shortOrg.app.shared.enumerate.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDto {
    private Long id;
    private String category;
    private String title;
    private String content;
    private User writer;
    private LocalDateTime meetingTime;
    private String locationName;
    private Double longitude;
    private Double latitude;
    private Integer capacity;
    private PostStatus state;
    private JoinMode joinMode;
    private LocalDateTime lastModified;
}
