package com.loopers.domain.pg;

import com.loopers.data.feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class PgGatewayService {

    private final PgGatewayRepository pgGatewayRepository;

    public PgGatewayService(PgGatewayRepository pgGatewayRepository) {
        this.pgGatewayRepository = pgGatewayRepository;
    }

    @Retry(name = "pgRetry", fallbackMethod = "fallbackRequestPay")
    @CircuitBreaker(name = "pgCircuit", fallbackMethod = "fallbackRequestPay")
    public PgPaymentInfo.Response requestPay(PgPaymentInfo.Request request){
        return pgGatewayRepository.requestPay(request);
    }

    public PgPaymentInfo.Response fallbackRequestPay(PgPaymentInfo.Request request, RetryableException e){
        return new PgPaymentInfo.Response(
                null,
                new PgPaymentInfo.TransactionData(
                        null,
                        PgPaymentInfo.Status.FAILED,
                        "결제 서버 오류"
                )
        );
    }

    @Retry(name = "pgRetry", fallbackMethod = "fallbackCheckTransactionStatus")
    public PgSourceInfo.PgTransactionDetail checkTransactionStatus(String transactionKey) {
        return pgGatewayRepository.checkTransactionStatus(transactionKey).transactionData();
    }

    @Retry(name = "pgRetry", fallbackMethod = "fallbackCheckOrderStatus")
    public PgSourceInfo.ResponseByOrderId checkOrderStatus(String orderId) {
        return pgGatewayRepository.checkOrderStatus(orderId);
    }

    public PgPaymentInfo.Response fallbackCheckTransactionStatus(String transactionKey, RetryableException e){
        throw new RuntimeException("CheckTransaction API 오류 발생. ", e);
    }

    public PgPaymentInfo.Response fallbackCheckOrderStatus(String transactionKey, RetryableException e){
        throw new RuntimeException("CheckOrder API 오류 발생. ", e);
    }

}
