package com.loopers.data.feign;

import com.loopers.domain.pg.PgPaymentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "pgClient",
        url = "http://localhost:8082",
        configuration = FeignClientConfig.class
)
public interface PgFeignClient {

    @PostMapping(value = "/api/v1/payments", produces = "application/json", consumes = "application/json")
    PgPaymentInfo.ReqResponse requestPayment(@RequestHeader("X-USER-ID") String userId, @RequestBody PgPaymentInfo.Request request);


    @GetMapping(value = "/api/v1/payments/{transactionKey}", consumes = "applciation/json")
    PgPaymentInfo.TransactionResponse.Data getTransaction(@RequestHeader("X-USER-ID") String userId, @PathVariable String transactionKey);

    @GetMapping(value = "/api/v1/payments", consumes = "applciation/json")
    PgPaymentInfo.OrderResponse.Data getTransactionByOrder(@RequestParam String orderId);
}
