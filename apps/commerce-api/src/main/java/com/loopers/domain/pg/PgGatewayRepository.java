package com.loopers.domain.pg;

public interface PgGatewayRepository {

    PgPaymentInfo.ReqResponse requestPay(PgPaymentInfo.Request request);

    PgPaymentInfo.TransactionResponse.Data checkTransaction(String transactionKey);

    PgPaymentInfo.OrderResponse.Data checkOrder(String orderId);

}
