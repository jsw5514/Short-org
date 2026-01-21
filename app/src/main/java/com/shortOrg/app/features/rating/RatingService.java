package com.shortOrg.app.features.rating;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.Rating;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.repository.RatingRepository;
import com.shortOrg.app.repository.UserRepository;
import com.shortOrg.app.shared.dto.RatingRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {
    private final RatingRepository ratingRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public void ratingEvaluation(Long postId, RatingRequest ratingRequest, String name) {
        boolean check = ratingRepository.existsByPostIdAndRaterIdAndTargetId(
                entityManager.getReference(Post.class, postId),
                (entityManager.getReference(User.class, name)),
                (entityManager.getReference(User.class, ratingRequest.getTargetUserId()))
        );

        if(!check){
            Rating rating = Rating.builder()
                    .postId(entityManager.getReference(Post.class, postId))
                    .raterId(entityManager.getReference(User.class, name))
                    .targetId(entityManager.getReference(User.class, ratingRequest.getTargetUserId()))
                    .score(ratingRequest.getScore())
                    .comment(ratingRequest.getComment())
                    .build();

            ratingRepository.save(rating);
        } else {
            throw new RuntimeException("이미 평가 했음");
        }
    }
}
