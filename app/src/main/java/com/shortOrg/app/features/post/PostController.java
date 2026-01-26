package com.shortOrg.app.features.post;

import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostResponse;
import com.shortOrg.app.shared.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    
    //전체 게시글 조회
    @GetMapping("")
    public ResponseEntity<List<PostResponse>> getAllPosts(Authentication auth) {
        return ResponseEntity.ok(postService.getAllPosts(auth.getName()));
    }

    // 카테고리별 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getPosts(@PathVariable("category") String category, Authentication auth){
        List<PostResponse> postResponse = postService.getPosts(category, auth.getName());
        if(!postResponse.isEmpty()) {
            return ResponseEntity.ok(postResponse);
        } else return null; // else 부분은 나중에 수정
    }

    // 게시글 등록
    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequest postCreate, Authentication authentication) {
        postService.createPost(authentication.getName(), postCreate);
        return ResponseEntity.ok("성공");
    }

    // 게시글 보기
    @GetMapping("/id/{postId}")
    public ResponseEntity<?> showPost(@PathVariable("postId") Long id, Authentication auth){
        try {
            PostResponse post = postService.showPost(id, auth.getName());
            return ResponseEntity.ok(post);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
        }
    }

    // 게시글 수정
    @PutMapping("/id/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long id, @RequestBody PostCreateRequest postRequest){
        postService.updatePost(id, postRequest);
        return ResponseEntity.ok("성공");
    }

    // 게시글 삭제
    @DeleteMapping("/id/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("삭제 완료");
    }

    // 지도로 게시글 보기
    @GetMapping("/nearby")
    public ResponseEntity<?> showNearby(
        @RequestParam("latitude") Double latitude,
        @RequestParam("longitude") Double longitude,
        @RequestParam(value = "radiusMeters", required = false) Integer radiusMeters,
        @RequestParam(value = "category", required = false) String category, 
        Authentication auth) {
        List<PostResponse> posts = postService.showNearby(latitude, longitude, radiusMeters, category, auth.getName());

        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/hot")
    public ResponseEntity<?> showHotPosts(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "radiusMeters", required = false) Integer radiusMeters
    ) {
        List<PostResponse> hotPosts = postService.getHotPosts(latitude, longitude, radiusMeters);
        return ResponseEntity.ok(hotPosts);
    }
}
