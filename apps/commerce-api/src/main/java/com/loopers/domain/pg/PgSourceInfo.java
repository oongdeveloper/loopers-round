package com.loopers.domain.pg;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class PgSourceInfo {
    public record Response(
            Object meta,
            @JsonProperty("data") PgTransactionDetail transactionData
    ){}

    public record PgTransactionDetail(
            String transactionKey,
            String orderId,
            CardType cardType,
            String cardNo,
            BigDecimal amount,
            Status status,
            String reason
    ){
        public enum CardType{
            SAMSUNG, KB, HYUNDAI
        }

        public enum Status{
            PENDING, FAILED, SUCCESS
        }
    }

    public record ResponseByOrderId(
            @JsonProperty("meta") Meta meta,
            @JsonProperty("data") PgOrderTransactionDetail transactionData
    ){}

    public record PgOrderTransactionDetail(
            String orderId,
            List<PgTransactionDetail> transactions
    ){}

    public record Meta(
            MetaStatus result,
            String errorCode,
            String message
    ){}

    public enum MetaStatus{
        FAIL, SUCCESS
    }
}
