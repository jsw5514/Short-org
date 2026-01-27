package com.shortOrg.app.features.post;

import com.shortOrg.app.domain.Applicant;
import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostResponse;
import com.shortOrg.app.features.post.mapper.PostMapper;
import com.shortOrg.app.repository.ApplicantRepository;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.repository.projection.PostDistanceView;
import com.shortOrg.app.shared.enumerate.ApplicantStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final ApplicantRepository applicantRepository;
    private final EntityManager entityManager;
    
    private PostResponse postToPostResponse(Post post, String userId) {
        Long currentCount = applicantRepository.countByPost_Id(post.getId());
        Applicant myParticipationStatus = applicantRepository.findByPostIdAndUserId(post.getId(), userId).orElse(null);
                
        return postMapper.fromEntity(post, currentCount, ApplicantStatus.toString(myParticipationStatus==null?null:myParticipationStatus.getState()));
    }

    //TODO N+1 쿼리 수정
    // 카테고리별 조회
    @Transactional(readOnly = true)
    public List<PostResponse> getPosts(String category, String userId) {
        List<Post> posts = postRepository.findByCategory(category);

        return convertToPostResponses(posts, userId);
    }

    @Transactional
    public void createPost(String userId, PostCreateRequest postCreate) {
        Post post = postMapper.fromCreateRequest(userId, postCreate);
        Post savedPost = postRepository.save(post);
        Applicant applicant = Applicant.builder()
                .post(savedPost)
                .user(entityManager.getReference(User.class, userId))
                .state(ApplicantStatus.HOST)
                .build();
        applicantRepository.save(applicant);
    }

    @Transactional(readOnly = true)
    public PostResponse showPost(Long id, String userId) {
        Post post = postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("잘못된 게시글 id"));
        return postToPostResponse(post, userId);
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

    //TODO N+1 쿼리 수정
    // 내 주변 조회
    @Transactional(readOnly = true)
    public List<PostResponse> showNearby(Double latitude, Double longitude, Integer radiusMeters, String category, String userId) {
        List<Post> posts = postRepository.findNearByPosts(longitude, latitude, radiusMeters, category);
        return convertToPostResponses(posts, userId);
    }

    //TODO N+1 쿼리 수정
    // 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(String userId) {
        List<Post> posts = postRepository.findAll();
        return convertToPostResponses(posts, userId);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getHotPosts(Double latitude, Double longitude, Integer radiusMeters) {
        List<PostDistanceView> posts = postRepository.findAlmostFullOrderByDistance(latitude, longitude, radiusMeters);
        return posts.stream()
                .map(postMapper::fromDistanceView)
                .toList();
    }

    private List<PostResponse> convertToPostResponses(List<Post> posts, String userId){
        if(posts.isEmpty())
            return new ArrayList<>();

        // 게시글 Id 리스트 추출 (in 절에 넣기위해 ??)
        List<Long> postIds = posts.stream().map(Post::getId).toList();

        // 참여자 수 조회 (Map으로 변환)
        Map<Long, Long> countMap = applicantRepository.countByPostIn(postIds)
                .stream()
                .collect(Collectors.toMap(
                        obj -> (Long) obj[0], // postId
                        obj -> (Long) obj[1] // count
                ));

        // 내 참여 상태 조회 (Map으로 변환)
        Map<Long, Applicant> myApplicantMap = applicantRepository.findByUserIdAndPostIdIn(userId, postIds)
                .stream()
                .collect(Collectors.toMap(
                        app-> app.getPost().getId(),
                        app -> app
                ));

        // 메모리에서 매핑
        List<PostResponse> resultList = new ArrayList<>();
        for(Post post: posts){
            Long currentCount = countMap.getOrDefault(post.getId(), 0L);
            Applicant myApplicant = myApplicantMap.get(post.getId());

            resultList.add(postMapper.fromEntity(
                    post,
                    currentCount,
                    ApplicantStatus.toString(myApplicant == null ? null : myApplicant.getState())
            ));
        }

        return resultList;
    }
}
