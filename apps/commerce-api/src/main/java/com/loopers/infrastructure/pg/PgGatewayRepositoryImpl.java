package com.loopers.infrastructure.pg;

import com.loopers.data.feign.PgFeignClient;
import com.loopers.domain.pg.PgGatewayRepository;
import com.loopers.domain.pg.PgPaymentInfo;
import com.loopers.domain.pg.PgSourceInfo;
import org.springframework.stereotype.Repository;

@Repository
public class PgGatewayRepositoryImpl implements PgGatewayRepository {
    private final String ONG_SHOP_USER = "ONG_SHOP";

    private final PgFeignClient pgFeignClient;

    public PgGatewayRepositoryImpl(PgFeignClient pgFeignClient) {
        this.pgFeignClient = pgFeignClient;
    }

    @Override
    public PgPaymentInfo.Response requestPay(PgPaymentInfo.Request request) {
        return pgFeignClient.requestPayment(ONG_SHOP_USER, request);
    }

    @Override
    public PgSourceInfo.Response checkTransactionStatus(String transactionKey) {
        return pgFeignClient.getTransaction(ONG_SHOP_USER, transactionKey);
    }

    @Override
    public PgSourceInfo.ResponseByOrderId checkOrderStatus(String orderId) {
        return pgFeignClient.getTransactionByOrder(orderId);
    }
}
