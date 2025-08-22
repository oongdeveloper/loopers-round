package com.loopers.domain.payment.spec;

import com.loopers.domain.payment.Payment;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CardSpec implements PaySpec{
    private String cardNo;
    private String cardType;
    private Payment.Method method = Payment.Method.CARD;

    public CardSpec(String cardNo, String cardType){
        this.cardNo = cardNo;
        this.cardType = cardType;
    }

    @Override
    public Payment.Method getMethod() {
        return this.method;
    }

    public static CardSpec of(String cardNo, String cardType){
        return new CardSpec(cardNo, cardType);
    }
}
