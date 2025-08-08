package com.loopers.application.point;

import com.loopers.domain.point.Point;

import java.math.BigDecimal;

public record PointInfo (
        Long userId,
        BigDecimal point
){
    public static PointInfo from(Point point){
        return new PointInfo(point.getUserId(), point.getPoint());
    }
}
