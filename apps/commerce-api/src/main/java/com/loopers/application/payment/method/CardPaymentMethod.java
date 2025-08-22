package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.spec.CardSpec;
import com.loopers.domain.pg.PgGatewayService;
import com.loopers.domain.pg.PgPaymentInfo;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentMethod extends AbstractPaymentMethod {
    private final PgGatewayService pgGatewayService;
    private final String CALLBACK_URL = "http://localhost:8080/api/v1/payment/callback";

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
        PaymentResult paymentResult = new PaymentResult(
                command.getOrderId(),
                command.getIdempotencyKey(),
                command.getAmount(),
                command.getMethod().name(
                ));

        try{
            PgPaymentInfo.Response response = pgGatewayService.requestPay(
                    PgPaymentInfo.Request.of(
                            command.getIdempotencyKey(),
                            cardSpec.getCardType(),
                            cardSpec.getCardNo(),
                            command.getAmount(),
                            CALLBACK_URL
                    ));

            if(response.transactionData().status().equals(PgPaymentInfo.Status.FAILED)){
                setRequestPayFailed(paymentResult, response);
            } else if(response.transactionData().status().equals(PgPaymentInfo.Status.SUCCESS)) {
                setRequestPaySuccess(paymentResult, response);
            }
        } catch (RuntimeException e){
            paymentResult.setSuccess(false);
            paymentResult.setStatus(PaymentResult.Status.FAILED);
            paymentResult.setReason("결제 서버에 문제가 발생했습니다.");
        }

        return paymentResult;
    }

    private void setRequestPaySuccess(PaymentResult result, PgPaymentInfo.Response response){
        result.setSuccess(true);
        result.setPgProvider(1);
        result.setPgTransactionKey(response.transactionData().transactionKey());
    }

    private void setRequestPayFailed(PaymentResult result, PgPaymentInfo.Response response){
        result.setSuccess(false);
        result.setStatus(PaymentResult.Status.FAILED);
        result.setPgProvider(1);
        result.setPgTransactionKey(response.transactionData().transactionKey());
    }
}
