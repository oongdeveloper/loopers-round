package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentFacade;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.pg.PgSourceInfo;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController implements PaymentV1ApiSpec{
    private final PaymentFacade paymentFacade;

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @Override
    @PostMapping("/api/v1/payment")
    public ApiResponse<?> payment(@RequestHeader(value = "X-USER-ID") Long userId, @RequestBody PaymentV1Dto.Request request) {
        PaymentResult result = paymentFacade.pay(request.toCommand(userId));

        return ApiResponse.success(PaymentV1Dto.Response.of(result));
    }

//    @Override
    @PostMapping("/api/v1/payment/callback")
    public ApiResponse<?> callback(@RequestBody PgSourceInfo.PgTransactionDetail req) {
        System.out.println("콜백 요청됨");
        paymentFacade.postPg(req);
        return null;
    }

    @Override
    @PostMapping("/api/v1/payment/generatekey")
    public ApiResponse<String> generateKey(@RequestBody PaymentV1Dto.UniqueKeyReq keyRequest){
        String uniqueKey = paymentFacade.generateKey(keyRequest.userId(), keyRequest.orderId());
        return ApiResponse.success(uniqueKey);
    }
}
