package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.data.feign.ValidateParamException;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.spec.CardSpec;
import com.loopers.domain.pg.PgGatewayService;
import com.loopers.domain.pg.PgPaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CardPaymentMethod extends AbstractPaymentMethod {
    private final PgGatewayService pgGatewayService;
    private final String CALLBACK_URL = "http://localhost:8080/api/v1/payment/callback";
    private final int PG_PROVIDER = 1;

    public CardPaymentMethod(PgGatewayService pgGatewayService){
        this.pgGatewayService = pgGatewayService;
    }

    @Override
    public boolean supports(Payment.Method method) {
        return Payment.Method.CARD.equals(method);
    }

    @Override
    PaymentResult doPay(PaymentCommand command) {
        CardSpec cardSpec = (CardSpec) command.getSpec();
        try{
            PgPaymentInfo.ReqResponse reqResponse = pgGatewayService.requestPay(
                    PgPaymentInfo.Request.of(
                            command.getIdempotencyKey(),
                            cardSpec.getCardType(),
                            cardSpec.getCardNo(),
                            command.getAmount(),
                            CALLBACK_URL
                    ));

            // TODO. 고민이 필요함.
            return new PaymentResult(
                    command.getOrderId(),
                    command.getIdempotencyKey(),
                    command.getAmount(),
                    command.getMethod(),
                    null,
                    null,
                    PG_PROVIDER,
                    reqResponse.data().transactionKey()
            );
        } catch (ValidateParamException e){
            return new PaymentResult(
                    command.getOrderId(),
                    command.getIdempotencyKey(),
                    command.getAmount(),
                    command.getMethod(),
                    PaymentResult.Status.FAILED,
                    e.getMessage(),
                    PG_PROVIDER,
                    null
            );
        } catch (RuntimeException e){
            log.error("카드 결제 시 오류가 발생했습니다.", e);
            return new PaymentResult(
                    command.getOrderId(),
                    command.getIdempotencyKey(),
                    command.getAmount(),
                    command.getMethod(),
                    null,
                    null,
                    PG_PROVIDER,
                    null
            );
        }
    }
}
