package com.loopers.application.payment;

import com.loopers.domain.payment.Payment;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class PaymentStatusHandler {
    private static final Map<Payment.Status, Runnable> statusHandlers = ofEntries(
            entry(Payment.Status.NEW, () -> {
                throw new IllegalArgumentException("처리 중인 결제 건이 존재합니다.");
            }),
            entry(Payment.Status.PENDING, () -> {
                throw new IllegalArgumentException("처리 중인 결제 건이 존재합니다.");
            }),
            entry(Payment.Status.COMPLETED, () -> {
                throw new IllegalArgumentException("이미 처리된 결제 건이 존재합니다.");
            }),
            entry(Payment.Status.CANCELED, () -> {
                throw new IllegalArgumentException("이미 처리된 결제 건이 존재합니다.");
            }),
            entry(Payment.Status.FAILED, () -> {
                throw new IllegalArgumentException("이미 처리된 결제 건이 존재합니다.");
            }),
            entry(Payment.Status.SYSTEM_FAILED, () -> {
                throw new IllegalArgumentException("처리 중인 결제 건이 존재합니다.");
            })
    );

    public static void handleExistingPayment(Payment.Status status) {
        if (statusHandlers.containsKey(status)){
            statusHandlers.get(status).run();
        }
    }
}
