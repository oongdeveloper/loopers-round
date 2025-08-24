package com.loopers.domain.pg;

import com.loopers.data.feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import static com.loopers.domain.pg.PgPaymentInfo.*;

@Service
public class PgGatewayService {

    private final PgGatewayRepository pgGatewayRepository;

    public PgGatewayService(PgGatewayRepository pgGatewayRepository) {
        this.pgGatewayRepository = pgGatewayRepository;
    }

    @Retry(name = "pgRetry", fallbackMethod = "fallbackRequestPay")
    @CircuitBreaker(name = "pgCircuit", fallbackMethod = "fallbackRequestPay")
    public ReqResponse requestPay(Request request){
        return pgGatewayRepository.requestPay(request);
    }

    public ReqResponse fallbackRequestPay(Request request, RetryableException e){
        return new ReqResponse(
                null,
                new ReqResponse.Data(
                        null,
                        ResStatus.FAILED,
                        "결제 서버 오류"
                )
        );
    }

    @Retry(name = "pgRetry", fallbackMethod = "fallbackCheckTransaction")
    public TransactionResponse.Data checkTransaction(String transactionKey) {
        return pgGatewayRepository.checkTransaction(transactionKey);
    }

    @Retry(name = "pgRetry", fallbackMethod = "fallbackCheckOrder")
    public OrderResponse.Data checkOrder(String orderId) {
        return pgGatewayRepository.checkOrder(orderId);
    }

    public ReqResponse fallbackCheckTransaction(String transactionKey, RetryableException e){
        throw new RuntimeException("CheckTransaction API 오류 발생. ", e);
    }

    public ReqResponse fallbackCheckOrder(String transactionKey, RetryableException e){
        throw new RuntimeException("CheckOrder API 오류 발생. ", e);
    }

}
