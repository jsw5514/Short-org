package com.shortOrg.app.repository;

import com.shortOrg.app.domain.Applicant;
import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    boolean existsByPostIdAndUserId(Post postId, User userId);

    @Query("select a from Applicant a where a.post.id = :post")
    List<Applicant> findByPostId(@Param("post") Long postId);
}
