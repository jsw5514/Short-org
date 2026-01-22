package com.shortOrg.app.features.post;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.shared.dto.JoinMode;
import com.shortOrg.app.shared.dto.PostCreateRequest;
import com.shortOrg.app.shared.dto.PostDto;
import com.shortOrg.app.shared.dto.PostStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final EntityManager entityManager;

    public List<PostDto> getPosts(String category) {
        List<Post> posts = postRepository.findByCategory(category);
        List<PostDto> postDtoList = new ArrayList<>();

        for (Post post: posts) {
            PostDto dto = PostDto.builder()
                    .id(post.getId())
                    .category(post.getCategory())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .writerId(post.getWriterId())
                    .meetingTime(post.getMeetingTime())
                    .locationName(post.getLocationName())
                    .longitude(post.getLongitude())
                    .latitude(post.getLatitude())
                    .capacity(post.getCapacity())
                    .state(post.getState())
                    .joinMode(post.getJoinMode())
                    .lastModified(post.getLastModified())
                    .build();
            postDtoList.add(dto);
        }

        return postDtoList;
    }

    public void createPost(String userId, PostCreateRequest postCreate) {
        Post post = new Post();
        post.setId(null);
        post.setCategory(postCreate.getCategory());
        post.setTitle(postCreate.getTitle());
        post.setContent(postCreate.getContent());
        post.setMeetingTime(postCreate.getMeetingTime());
        post.setLocationName(postCreate.getLocationName());
        post.setLongitude(postCreate.getLongitude());
        post.setLatitude(postCreate.getLatitude());
        post.setCapacity(postCreate.getCapacity());
        post.setJoinMode(postCreate.getJoinMode());
        post.setState(PostStatus.OPEN);
        post.setWriterId(entityManager.getReference(User.class, userId));
        postRepository.save(post);
    }

    public Post showPost(Long id) {
        Optional<Post> oPost =  postRepository.findById(id);
        Post post = null;
        if(oPost.isPresent()) {
            post = oPost.get();
        }
        return post;
    }

    // 게시글 수정
    public void updatePost(Long id, PostCreateRequest postCreate) {
        // 기존 게시글 조회
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없음"));

        // 데이터 업데이트
        post.setCategory(postCreate.getCategory());
        post.setTitle(postCreate.getTitle());
        post.setContent(postCreate.getContent());
        post.setMeetingTime(postCreate.getMeetingTime());
        post.setLocationName(postCreate.getLocationName());
        post.setLongitude(postCreate.getLongitude());
        post.setLatitude(postCreate.getLatitude());
        post.setCapacity(postCreate.getCapacity());
        post.setJoinMode(post.getJoinMode());

        post.setLastModified(null);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없음"));

        postRepository.delete(post);
    }


    public List<Post> showNearby(Double latitude, Double longitude, Integer radiusMeters, String category) {
        return postRepository.findNearByPosts(longitude, latitude, radiusMeters, category);
    }
}
