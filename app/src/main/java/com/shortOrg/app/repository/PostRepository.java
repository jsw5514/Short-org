package com.shortOrg.app.repository;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.repository.projection.PostDistanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostDistanceRepository {

    List<Post> findByCategory(String category);

    /*
    쿼리 설명
    -사용한 함수
    POINT(:lon, :lat) -> 입력받은 데이터를 나타내는 점
    ST_SRID(..., 4326) -> 이 점은 SRID 4326(=WGS84, 구글맵에서 사용하는 좌표계)의 좌표계에 해당하는 점이라는 정보를 추가
    ST_Distance_Sphere(...) -> 두 점 사이의 거리, 여기에선 DB에 저장된 좌표(p.location)과 입력받은 경위도 좌표(ST_SRID(POINT(:lon, :lat), 4326)) 간의 거리를 구했음
    
    -사용한 조건
    ST_Distance_Sphere(...) <= radius --> 두 점 사이의 거리가 radius(지정한 반경 거리값, 미터)보다 작은 행만 검색(=반경 (radius)m 이내의 게시글만 검색)
    
    */
    @Query(value = """
        SELECT *
        FROM (
            SELECT p.*,
                   ST_Distance_Sphere(p.location, ST_SRID(POINT(:lon, :lat), 4326)) AS distance
            FROM post p
            WHERE (:category IS NULL OR p.category = :category)
        ) t
        WHERE t.distance <= :radius
        ORDER BY t.distance ASC
    """, nativeQuery = true)
    List<Post> findNearByPosts(
            @Param("lon") double lon,
            @Param("lat") double lat,
            @Param("radius") int radius,
            @Param("category") String category
    );
}
