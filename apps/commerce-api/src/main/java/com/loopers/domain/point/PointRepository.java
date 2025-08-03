package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {
    Optional<Point> find(String findId);

    Point save(Point Entity);
}
