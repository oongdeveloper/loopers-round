package com.loopers.domain.payment;


import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payment",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ref_order_id", "type"}) // 한 SKU에 동일 옵션 이름 중복 방지
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class Payment extends BaseEntity {
    @Column(name = "ref_order_id", nullable = false)
    Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    Type paymentType;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    BigDecimal amount;

    public static Payment of(Long orderId, Type paymentType, BigDecimal amount) {
        return new Payment(orderId, paymentType, amount);
    }

    public enum Type{
        POINT,
        CARD
    }

}
