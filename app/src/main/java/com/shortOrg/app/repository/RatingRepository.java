package com.shortOrg.app.repository;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.Rating;
import com.shortOrg.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByPostIdAndRaterIdAndTargetId(Post post, User raterId, User targetId);
}
