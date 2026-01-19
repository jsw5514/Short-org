package com.shortOrg.app.features.rating;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.Rating;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.repository.RatingRepository;
import com.shortOrg.app.repository.UserRepository;
import com.shortOrg.app.shared.dto.RatingRequest;
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

    public void ratringEvaluation(Long postId, RatingRequest ratingRequest, String name) {
        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("게시글 불러오기 실패"));
        User raterId = userRepository.findById(name).orElseThrow(() -> new RuntimeException("평가하는 사람 불러오기 실패"));
        User targetId = userRepository.findById(ratingRequest.getTargetUserId()).orElseThrow(()-> new RuntimeException("평가 받는 사람 불러오기 실패"));

        boolean check = ratingRepository.existsByPostIdAndRaterIdAndTargetId(post, raterId, targetId);

        if(!check){
            Rating rating = Rating.builder()
                    .postId(post)
                    .raterId(raterId)
                    .targetId(targetId)
                    .score(ratingRequest.getScore())
                    .comment(ratingRequest.getComment())
                    .build();

            ratingRepository.save(rating);
        } else {
            throw new RuntimeException("이미 평가 했음");
        }
    }
}
