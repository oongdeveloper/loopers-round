package com.loopers.domain.payment;

import lombok.Getter;

import java.util.UUID;

public class PaymentKeyGenerator {
    private static final String PREFIX = "ONGSHOP-";

    public static UniquePaymentKey generate() {
        String uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
        return new UniquePaymentKey(PREFIX + uuid);
    }

    @Getter
    public static class UniquePaymentKey{
        private String key;

        public UniquePaymentKey(String key) {
            this.key = key;
        }
    }
}
