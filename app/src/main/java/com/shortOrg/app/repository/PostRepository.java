package com.shortOrg.app.repository;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategory(String category);
}
