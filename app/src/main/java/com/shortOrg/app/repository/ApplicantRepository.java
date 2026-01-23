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
    boolean existsByPostAndUser(Post post, User user);

    @Query("select a from Applicant a where a.post.id = :post")
    List<Applicant> findByPostId(@Param("post") Long postId);

    Applicant findByPostIdAndUserId(Long postId, String userId);
    
    long deleteByPost_IdAndUser_Id(Long postId, String userId);
}
