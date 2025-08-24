package com.loopers.domain.payment;


import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@ToString
public class Payment extends BaseEntity {

    @Column(name = "idempotency_key", unique = true, nullable = false)
    String idempotencyKey;

    @Column(name = "ref_user_id", nullable = false)
    Long userId;

    @Column(name = "ref_order_id", nullable = false)
    Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    Method method;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    Status status;

    @Column(name = "reason")
    String reason;

    @Column(name = "pg_provider")
    int pgProvider;

    @Column(name = "pg_transaction_id")
    String pgTransactionId;

    private Payment(Long userId, Long orderId, String idempotencyKey, Method method, BigDecimal amount, Status status){
        this.userId = userId;
        this.orderId = orderId;
        this.idempotencyKey = idempotencyKey;
        this.method = method;
        this.amount = amount;
        this.status = status;
    }

    public static Payment of(PaymentCommand command) {
        return new Payment(
                command.getUserId(),
                command.getOrderId(),
                command.getIdempotencyKey(),
                command.getMethod(),
                command.getAmount(),
                Status.NEW
        );
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void setPgProvider(int provider){
        this.pgProvider = provider;
    }

    public void setPgTransactionId(String id){
        this.pgTransactionId = id;
    }

    public void setReason(String reason){
        this.reason = reason;
    }

    public enum Method{
        POINT,
        CARD;

        public static  Method from(String name){
            if(name == null || name.isBlank()){
                throw new IllegalArgumentException("결제 수단은 필수 항목입니다.");
            }

            try{
                return Method.valueOf(name.toUpperCase());
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("존재하지 않는 결제 수단입니다: " + name);
            }
        }
    }

    public enum Status{
        NEW,
        PENDING,
        FAILED,
        SYSTEM_FAILED,
        COMPLETED,
        CANCELED
    }

}
