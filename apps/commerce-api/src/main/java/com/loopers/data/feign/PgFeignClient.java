package com.loopers.data.feign;

import com.loopers.domain.pg.PgPaymentInfo;
import com.loopers.domain.pg.PgSourceInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "pgClient",
        url = "http://localhost:8082",
        configuration = FeignClientConfig.class
)
public interface PgFeignClient {

    @PostMapping(value = "/api/v1/payments", produces = "application/json", consumes = "application/json")
    PgPaymentInfo.Response requestPayment(@RequestHeader("X-USER-ID") String userId, @RequestBody PgPaymentInfo.Request request);


    @GetMapping(value = "/api/v1/payments/{transactionKey}", consumes = "applciation/json")
    PgSourceInfo.Response getTransaction(@RequestHeader("X-USER-ID") String userId, @PathVariable String transactionKey);

    @GetMapping(value = "/api/v1/payments", consumes = "applciation/json")
    PgSourceInfo.ResponseByOrderId getTransactionByOrder(@RequestParam String orderId);
}
