package com.shortOrg.app.repository;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategory(String category);

    @Query(value = "SELECT * FROM post p " +
            "WHERE (:radius IS NULL OR (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:lon)) + sin(radians(:lat)) * " +
            "sin(radians(p.latitude)))) <= :radius / 1000.0) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:lon)) + sin(radians(:lat)) * " +
            "sin(radians(p.latitude)))) ASC", nativeQuery = true)
    List<Post> findNearByPosts(
            @Param("lon") Double lon,
            @Param("lat") Double lat,
            @Param("radius") Integer radius,
            @Param("category") String category
    );
}
