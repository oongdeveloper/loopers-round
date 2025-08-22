package com.loopers.domain.pg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import java.math.BigDecimal;

public class PgPaymentInfo {

    public record Request(
            String orderId,
            String cardType,
            String cardNo,
            BigDecimal amount,
            String callbackUrl
    ){
        public static Request of(String orderId, String cardType, String cardNo, BigDecimal amount, String callbackUrl){
            return new Request(
                    orderId,
                    cardType,
                    cardNo,
                    amount,
                    callbackUrl
            );
        }

        public Request{
            validate(orderId, cardNo, amount);
        }

        private void validate(String orderId, String cardNo, BigDecimal amount){
            String REGEX_CARD_NO = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";

            if (orderId.isBlank() || orderId.length() < 6) {
                throw new CoreException(ErrorType.BAD_REQUEST, "주문 ID는 6자리 이상 문자열이어야 합니다.");
            }
            if (!cardNo.matches(REGEX_CARD_NO)) {
                throw new CoreException(ErrorType.BAD_REQUEST, "카드 번호는 xxxx-xxxx-xxxx-xxxx 형식이어야 합니다.");
            }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new CoreException(ErrorType.BAD_REQUEST, "결제금액은 양의 정수여야 합니다.");
            }

        }
    }

    public record Response(
//            String transactionKey,
//            Status status,
//            String reason
            Object meta,
            @JsonProperty("data") TransactionData transactionData
    ){}

    public record TransactionData(
            String transactionKey,
            Status status,
            String reason
    ){}

    public enum Status{
        PENDING,
        SUCCESS,
        FAILED,
        ;
    }
}
