package com.shortOrg.app.features.rating;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.shared.dto.RatingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/{postId}/ratings")
    public ResponseEntity<?> ratingEvaluation(@PathVariable Long postId, @RequestBody RatingRequest ratingRequest, Authentication auth) {
        String name = auth.getName();
        ratingService.ratringEvaluation(postId, ratingRequest, name);

        return ResponseEntity.ok("평가 성공");
    }
}
