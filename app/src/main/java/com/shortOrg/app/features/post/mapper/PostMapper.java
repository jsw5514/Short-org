package com.shortOrg.app.features.post.mapper;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostResponse;
import com.shortOrg.app.features.user.dto.UserSummary;
import com.shortOrg.app.features.user.mapper.UserMapper;
import com.shortOrg.app.shared.enumerate.PostStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostMapper {
    private final EntityManager entityManager;
    private final UserMapper userMapper;

    public PostResponse fromEntity(Post post) {
        UserSummary userSummary = userMapper.fromEntity(post.getWriter());

        return PostResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(userSummary)
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
