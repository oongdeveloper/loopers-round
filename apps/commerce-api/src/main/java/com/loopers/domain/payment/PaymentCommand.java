package com.loopers.domain.payment;

import com.loopers.domain.payment.spec.PaySpec;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
public class PaymentCommand {
    private Long userId;
    private Long orderId;
    private String idempotencyKey;
    private BigDecimal amount;
    private Payment.Method method;
    private PaySpec spec;

    public PaymentCommand(Long userId, Long orderId, String idempotencyKey, BigDecimal amount, String method, PaySpec spec) {
        this.userId = userId;
        this.orderId = orderId;
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.method = Payment.Method.from(method);
        this.spec = spec;
    }

    public static PaymentCommand of(Long userId, Long orderId, String idempotencyKey, BigDecimal amount, String method, PaySpec spec){
        return new PaymentCommand(
                userId,
                orderId,
                idempotencyKey,
                amount,
                method,
                spec
        );
    }

    private static void validate(String idempotencyKey, BigDecimal amount){
        if (idempotencyKey.isBlank() || idempotencyKey.length() < 0){
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 ID 는 6자리 이상 문자열이어야 합니다.");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0){
            throw new CoreException(ErrorType.BAD_REQUEST, "결제금액은 양의 정수여야 합니다");
        }
    }
}
