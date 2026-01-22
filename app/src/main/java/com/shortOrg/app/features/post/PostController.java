package com.shortOrg.app.features.post;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.features.post.dto.PostCreateRequest;
import com.shortOrg.app.features.post.dto.PostDto;
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

    // 카테고리별 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getPosts(@PathVariable("category") String category){
        List<PostDto> postDto = postService.getPosts(category);
        if(!postDto.isEmpty()) {
            return ResponseEntity.ok(postDto);
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
    public ResponseEntity<?> showPost(@PathVariable("postId") Long id){
        Post post = postService.showPost(id);
        return ResponseEntity.ok(post);
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
        @RequestParam(value = "category", required = false) String category) {
        List<Post> posts = postService.showNearby(latitude, longitude, radiusMeters, category);

        return ResponseEntity.ok(posts);
    }
}
