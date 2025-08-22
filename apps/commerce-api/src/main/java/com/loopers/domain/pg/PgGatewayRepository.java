package com.loopers.domain.pg;

public interface PgGatewayRepository {

    PgPaymentInfo.Response requestPay(PgPaymentInfo.Request request);

    PgSourceInfo.Response checkTransactionStatus(String transactionKey);

    PgSourceInfo.ResponseByOrderId checkOrderStatus(String orderId);

}
