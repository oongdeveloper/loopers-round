package com.loopers.domain.pg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import java.math.BigDecimal;
import java.util.List;

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
//            validate(orderId, cardNo, amount);
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


    public record Meta(
            MetaStatus result,
            String errorCode,
            String message
    ){
        public enum MetaStatus{
            FAIL, SUCCESS
        }
    }
    public enum ResStatus{
        PENDING,
        SUCCESS,
        FAILED,
        ;
    }

    public record ReqResponse(
            @JsonProperty("meta") Meta meta,
            @JsonProperty("data") Data data
    ){
        public record Data(
                String transactionKey,
                ResStatus status,
                String reason
        ){}
    }

    public record TransactionResponse(
            @JsonProperty("meta") Meta meta,
            @JsonProperty("data") Data transactionData
    ){
        public record Data(
                String transactionKey,
                String orderId,
                CardType cardType,
                String cardNo,
                BigDecimal amount,
                ResStatus status,
                String reason
        ){
            public enum CardType{
                SAMSUNG, KB, HYUNDAI
            }
        }
    }

    public record OrderResponse(
            @JsonProperty("meta") Meta meta,
            @JsonProperty("data") Data transactionData
    ){
        public record Data(
                String orderId,
                List<TransactionResponse.Data> transactions
        ){}
    }
}
