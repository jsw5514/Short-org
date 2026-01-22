package com.shortOrg.app.features.post.mapper;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostResponse;
import com.shortOrg.app.shared.enumerate.PostStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final EntityManager entityManager;

    public PostResponse fromEntity(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getWriter())
                .meetingTime(post.getMeetingTime())
                .locationName(post.getLocationName())
                .longitude(post.getLongitude())
                .latitude(post.getLatitude())
                .capacity(post.getCapacity())
                .state(post.getState())
                .joinMode(post.getJoinMode())
                .lastModified(post.getLastModified())
                .build();
    }
    
    public Post fromCreateRequest(String userId, PostCreateRequest postCreate) {
        return Post.builder()
                .category(postCreate.getCategory())
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .meetingTime(postCreate.getMeetingTime())
                .locationName(postCreate.getLocationName())
                .longitude(postCreate.getLongitude())
                .latitude(postCreate.getLatitude())
                .capacity(postCreate.getCapacity())
                .joinMode(postCreate.getJoinMode())
                .state(PostStatus.OPEN)
                .writer(entityManager.getReference(User.class, userId))
                .build();

    }
}
