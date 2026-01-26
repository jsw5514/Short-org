package com.shortOrg.app.repository;

import com.shortOrg.app.repository.projection.PostDistanceView;

import java.util.List;

public interface PostDistanceRepository {
    List<PostDistanceView> findAlmostFullOrderByDistance(double latitude, double longitude, Integer radiusMeters);
}
