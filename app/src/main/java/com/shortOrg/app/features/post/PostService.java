package com.shortOrg.app.features.post;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.user.dto.PostCreateRequest;
import com.shortOrg.app.repository.PostRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    @Autowired
    PostRepository postRepository;
    private final EntityManager entityManager;

    public List<Post> getPosts(String category) {
        return postRepository.findByCategory(category);
    }


    public void createPost(PostCreateRequest postCreate) {
        Post post = new Post();
        post.setId(null);
        post.setCategory(postCreate.getCategory());
        post.setTitle(postCreate.getTitle());
        post.setContent(postCreate.getContent());
        post.setMettingTime(postCreate.getMeetingTime());
        post.setLongitude(postCreate.getLongitude());
        post.setLatitude(postCreate.getLatitude());
        post.setJoinMode(post.getJoinMode());

        post.setWriterId(entityManager.getReference(User.class, postCreate.getUserId()));
        post.setLastModified(null);

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

    public void updatePost(Long id, PostCreateRequest postCreate) {
        // 기존 게시글 조회
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없음"));

        // 데이터 업데이트
        post.setCategory(postCreate.getCategory());
        post.setTitle(postCreate.getTitle());
        post.setContent(postCreate.getContent());
        post.setMettingTime(postCreate.getMeetingTime());
        post.setLongitude(postCreate.getLongitude());
        post.setLatitude(postCreate.getLatitude());
        post.setJoinMode(post.getJoinMode());

        post.setLastModified(null);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없음"));

        postRepository.delete(post);
    }
}
