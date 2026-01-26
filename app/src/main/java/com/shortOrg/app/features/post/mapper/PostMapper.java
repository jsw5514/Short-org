package com.shortOrg.app.features.post.mapper;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostResponse;
import com.shortOrg.app.features.user.dto.UserSummary;
import com.shortOrg.app.features.user.mapper.UserMapper;
import com.shortOrg.app.repository.projection.PostDistanceView;
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
    
    public UserSummary postToUserSummary(Post post) {
        return UserSummary.builder()
                .id(post.getWriter().getId())
                .nickname(post.getWriterNickname())
                .profileImage(post.getWriterProfileImage())
                .build();
    }
    
    public UserSummary postDistanceToUserSummary(PostDistanceView postDistanceView) {
        return UserSummary.builder()
                .id(postDistanceView.getWriterId())
                .nickname(postDistanceView.getWriterNickname())
                .profileImage(postDistanceView.getWriterProfileImage())
                .build();
    }

    public PostResponse fromEntity(Post post, Long currentCount, String myParticipationStatus) {
        UserSummary userSummary = postToUserSummary(post);

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
                .currentCount(currentCount)
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
    
    public PostResponse fromDistanceView(PostDistanceView postDistanceView) {
        UserSummary userSummary = postDistanceToUserSummary(postDistanceView);
        
        return PostResponse.builder()
                .id(postDistanceView.getId())
                .category(postDistanceView.getCategory())
                .title(postDistanceView.getTitle())
                .content(postDistanceView.getContent())
                .writer(userSummary)
                .meetingTime(postDistanceView.getMeetingTime())
                .locationName(postDistanceView.getLocationName())
                .longitude(postDistanceView.getLongitude())
                .latitude(postDistanceView.getLatitude())
                .capacity(postDistanceView.getCapacity())
                .state(postDistanceView.getState())
                .joinMode(postDistanceView.getJoinMode())
                .lastModified(postDistanceView.getLastModified())
                .currentCount((long) (postDistanceView.getCapacity() - postDistanceView.getSlack()))
                .build();
    }
}
