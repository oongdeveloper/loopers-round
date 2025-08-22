package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.spec.CardSpec;
import com.loopers.domain.payment.spec.PaySpec;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class PaymentV1Dto {

    public record Request(
        Long orderId,
        String idempotencyKey,
        BigDecimal amount,
        Method method,
        String cardNo,
        String cardType
    ){
        public PaymentCommand toCommand(Long userId){
            PaySpec spec = null;
            if (method.equals(Method.CARD))
                spec = CardSpec.of(this.cardNo, this.cardType);

            return PaymentCommand.of(
                    userId,
                    orderId,
                    idempotencyKey,
                    amount,
                    method.name(),
                    spec
            );
        }
    }

    public record UniqueKeyReq(
            Long userId,
            Long orderId
    ){}

    public record Response(
            Long orderId,
            String idempotencyKey,
            BigDecimal amount,
            Method method,
            PaymentResult.Status status,
            String reason
    ){
        public static Response of(PaymentResult result){
            return new Response(
                    result.orderId(),
                    result.idempotencyKey(),
                    result.amount(),
                    Method.valueOf(result.method().name()),
                    result.status(),
                    result.reason()
            );
        }
    }

    private enum Method{
        CARD, POINT
    }
}
