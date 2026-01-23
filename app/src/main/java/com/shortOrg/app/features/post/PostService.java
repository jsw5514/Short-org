package com.shortOrg.app.features.post;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.features.post.mapper.PostMapper;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<PostResponse> getPosts(String category) {
        List<Post> posts = postRepository.findByCategory(category);

        return posts.stream()
                    .map(PostResponse::new)
                    .collect(Collectors.toList());
    }

    public void createPost(String userId, PostCreateRequest postCreate) {
        Post post = postMapper.fromCreateRequest(userId, postCreate);
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
        post.setLocationLngLat(postCreate.getLongitude(), postCreate.getLatitude());
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

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(postMapper::fromEntity).toList();
    }
}
