package com.loopers.application.point;

import com.loopers.domain.user.UserEntity;

public record PointInfo (
        String userId,
        long point
){
    public static PointInfo from(UserEntity user){
        return new PointInfo(user.getUserId(), user.getPoint());
    }
}
