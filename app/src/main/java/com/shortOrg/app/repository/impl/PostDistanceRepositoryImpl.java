package com.shortOrg.app.repository.impl;

import com.shortOrg.app.repository.PostDistanceRepository;
import com.shortOrg.app.repository.projection.PostDistanceView;
import com.shortOrg.app.shared.enumerate.JoinMode;
import com.shortOrg.app.shared.enumerate.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostDistanceRepositoryImpl implements PostDistanceRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<PostDistanceView> findAlmostFullOrderByDistance(double latitude, double longitude, Integer radiusMeters) {
        final String sql = """
            SELECT *
            FROM (SELECT p.*,
                    (p.capacity - COUNT(a.id)) AS slack,
                           ST_X(p.location) AS longitude,
                           ST_Y(p.location) AS latitude,
                    ST_Distance_Sphere(p.location, ST_SRID(POINT(:lon, :lat), 4326)) AS distance
            FROM post p
            LEFT JOIN applicant a ON a.post_id = p.id
            GROUP BY p.id) t
            WHERE t.slack <= 2
            AND t.distance <= :radiusMeters
            ORDER BY distance
        """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("lon", longitude)
                .addValue("lat", latitude)
                .addValue("radiusMeters", radiusMeters);
        
        return namedParameterJdbcTemplate.query(sql, params, (rs,rowCount)->new PostDistanceView(
                rs.getLong("id"),
                rs.getString("writer_id"),
                rs.getString("writer_nickname"),
                rs.getString("writer_profile_image"),
                rs.getString("category"),
                rs.getString("title"),
                rs.getString("content"),
                PostStatus.valueOf(rs.getString("state")),
                JoinMode.valueOf(rs.getString("join_mode")),
                rs.getObject("last_modified", LocalDateTime.class),
                rs.getObject("date_time", LocalDateTime.class),
                rs.getString("location_name"),
                rs.getDouble("longitude"),
                rs.getDouble("latitude"),
                rs.getInt("capacity"),
                rs.getInt("slack"),
                rs.getDouble("distance")
        ));
    }
}
