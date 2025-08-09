package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {
    Optional<Point> find(Long findId);

    Optional<Point> findForUpdate(Long findId);

    Point save(Point Entity);
}
