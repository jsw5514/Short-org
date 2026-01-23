package com.shortOrg.app.features.post.dto;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.features.user.dto.UserSummary;
import com.shortOrg.app.shared.enumerate.JoinMode;
import com.shortOrg.app.shared.enumerate.PostStatus;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResponse {
    private Long id;
    private String category;
    private String title;
    private String content;
    private UserSummary writer;
    private LocalDateTime meetingTime;
    private String locationName;
    private Double longitude;
    private Double latitude;
    private Integer capacity;
    private PostStatus state;
    private JoinMode joinMode;
    private LocalDateTime lastModified;

    public PostResponse(Post post){
        this.id = post.getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.meetingTime = post.getMeetingTime();
        this.locationName = post.getLocationName();
        this.longitude = post.getLongitude();
        this.latitude = post.getLatitude();
        this.capacity = post.getCapacity();
        this.state = post.getState();
        this.joinMode = post.getJoinMode();
        this.lastModified = post.getLastModified();

        if(post.getWriter() != null) {
            this.writer = new UserSummary(post.getWriter());
        }
    }
}
