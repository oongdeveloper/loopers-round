package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "point",uniqueConstraints = {
    @UniqueConstraint(name = "uq_user_id", columnNames = "ref_user_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point extends BaseEntity {

    @Column(name = "ref_user_id", nullable = false)
    Long userId;

    @Column(name = "point", nullable = false, precision = 12, scale = 2)
    BigDecimal point;

    private Point(Long userId, BigDecimal initialPoint) {
        if (userId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "사용자 ID는 필수입니다.");
        }
        if (initialPoint == null || initialPoint.compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "초기 포인트는 음수일 수 없습니다.");
        }
        this.userId = userId;
        this.point = initialPoint.setScale(2, RoundingMode.HALF_UP);
    }

    public static Point from(Long userId, BigDecimal initialPoint) {
        return new Point(userId, initialPoint);
    }

    public BigDecimal charge(BigDecimal chargePoint){
        if (chargePoint == null || chargePoint.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "충전 금액은 0보다 커야 합니다.");
        }
        this.point = this.point.add(chargePoint).setScale(2, RoundingMode.HALF_UP);
        return this.point;
    }

    public BigDecimal deduct(BigDecimal deductPoint){
        if (deductPoint == null || deductPoint.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "사용할 포인트는 0보다 커야 합니다.");
        }

        if (this.point.compareTo(deductPoint) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다.");
        }
        this.point = this.point.subtract(deductPoint).setScale(2, RoundingMode.HALF_UP);
        return this.point;
    }
}
