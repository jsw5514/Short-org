package com.shortOrg.app.features.post;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostResponse;
import com.shortOrg.app.features.post.mapper.PostMapper;
import com.shortOrg.app.repository.ApplicantRepository;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.repository.projection.PostDistanceView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final ApplicantRepository applicantRepository;
    
    private PostResponse postToPostResponse(Post post) {
        Long currentCount = applicantRepository.countByPost_Id(post.getId());
        return postMapper.fromEntity(post, currentCount);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPosts(String category) {
        List<Post> posts = postRepository.findByCategory(category);
        return posts.stream()
                .map(this::postToPostResponse)
                .toList();
    }

    @Transactional
    public void createPost(String userId, PostCreateRequest postCreate) {
        Post post = postMapper.fromCreateRequest(userId, postCreate);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse showPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("잘못된 게시글 id"));
        return postToPostResponse(post);
    }

    @Transactional
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

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없음"));
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> showNearby(Double latitude, Double longitude, Integer radiusMeters, String category) {
        List<Post> posts = postRepository.findNearByPosts(longitude, latitude, radiusMeters, category);
        return posts.stream().map(this::postToPostResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::postToPostResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getHotPosts(Double latitude, Double longitude, Integer radiusMeters) {
        List<PostDistanceView> posts = postRepository.findAlmostFullOrderByDistance(latitude, longitude, radiusMeters);
        return posts.stream()
                .map(postMapper::fromDistanceView)
                .toList();
    }
}
