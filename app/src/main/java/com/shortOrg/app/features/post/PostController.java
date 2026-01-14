package com.shortOrg.app.features.post;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.features.user.dto.PostCreateRequest;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/posts")
@RestController
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getPosts(@PathVariable("category") String category){
        try {
            List<Post> post = postService.getPosts(category);
            if(!post.isEmpty()) {
                return ResponseEntity.ok(post);
            } else return null; // else 부분은 나중에 수정
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실패");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequest postCreate, Authentication authentication) {
        try {
            postService.createPost(authentication.getName(), postCreate);
            return ResponseEntity.ok("성공");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/id/{postId}")
    public ResponseEntity<?> showPost(@PathVariable("postId") Long id){
        try {
            Post post = postService.showPost(id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/id/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long id, @RequestBody PostCreateRequest postRequest){
        try {
            postService.updatePost(id, postRequest);
            return ResponseEntity.ok("성공");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/id/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
