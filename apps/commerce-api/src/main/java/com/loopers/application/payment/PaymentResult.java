package com.loopers.application.payment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class PaymentResult {
    private Long orderId;
    private String idempotencyKey;
    private BigDecimal amount;
    private String method;

    @Setter
    private boolean isSuccess;
    @Setter
    private Status status;
    @Setter
    private String reason;
    @Setter
    private Integer pgProvider;
    @Setter
    private String pgTransactionKey;

    public PaymentResult(Long orderId, String idempotencyKey, BigDecimal amount, String method) {
        this.orderId = orderId;
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.method = method;
    }

    public Immutable getImmutable(){
        return new Immutable(
                this.orderId,
                this.idempotencyKey,
                this.amount,
                this.method,
                this.isSuccess,
                this.status,
                this.reason,
                this.pgProvider,
                this.pgTransactionKey
        );
    }

    public record Immutable(
            Long orderId,
            String idempotencyKey,
            BigDecimal amount,
            String method,
            boolean isSuccess,
            Status status,
            String reason,
            Integer pgProvider,
            String pgTransactionKey
    ){}

    public enum Status{
        NEW,
        PENDING,
        FAILED,
        SYSTEM_FAILED,
        COMPLETED,
        CANCELED
    }
}
