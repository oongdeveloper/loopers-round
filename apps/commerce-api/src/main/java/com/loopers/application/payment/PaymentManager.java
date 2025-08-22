package com.loopers.application.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.loopers.domain.payment.Payment.*;
import static java.util.Map.*;

@Component
public class PaymentManager {
    private final PaymentService paymentService;

    public PaymentManager(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Payment find(PaymentCommand command) {
        return paymentService.findByKey(command.getIdempotencyKey())
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "결제 이력이 존재하지 않습니다."));
    }

    public Payment create(PaymentCommand command) {
        // 이미 결제 진행 중인 건이 존재하는지 확인
        paymentService.findByKey(command.getIdempotencyKey()).ifPresent(
                payment -> PaymentStatusHandler.handleExistingPayment(payment.getStatus())
        );

        Payment payment = of(command);
        return paymentService.save(payment);
    }

    @Transactional
    public void updateStatus(String key, Status status) {
        paymentService.findByKey(key)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "결제 이력이 존재하지 않습니다."))
                .updateStatus(status);
    }

    @Transactional
    public void updateStatus(String key, PaymentResult.Immutable result) {
        Payment payment = paymentService.findByKey(key)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "결제 이력이 존재하지 않습니다."));

        payment.updateStatus(Status.valueOf(result.status().name()));
//        if(result.isSuccess()){
//            payment.updateStatus(Status.COMPLETED);
//        } else {
//            if (result.status().equals(PaymentResult.Status.FAILED)) payment.updateStatus(Status.FAILED);
//            else if (result.status().equals(PaymentResult.Status.SYSTEM_FAILED)) payment.updateStatus(Status.SYSTEM_FAILED);
//        }

        if(result.reason() != null) payment.setReason(result.reason());
        if(result.pgProvider() != null) payment.setPgProvider(result.pgProvider());
        if(result.pgTransactionKey() != null) payment.setPgTransactionId(result.pgTransactionKey());
    }

    class PaymentStatusHandler{
        private static final Map<Status, Runnable> statusHandlers = ofEntries(
                entry(Status.NEW, () -> {
                    throw new IllegalArgumentException("처리 중인 결제 건이 존재합니다.");
                }),
                entry(Status.PENDING, () -> {
                    throw new IllegalArgumentException("처리 중인 결제 건이 존재합니다.");
                }),
                entry(Status.COMPLETED, () -> {
                    throw new IllegalArgumentException("이미 처리된 결제 건이 존재합니다.");
                }),
                entry(Status.CANCELED, () -> {
                    throw new IllegalArgumentException("이미 처리된 결제 건이 존재합니다.");
                }),
                entry(Status.FAILED, () -> {
                    throw new IllegalArgumentException("이미 처리된 결제 건이 존재합니다.");
                }),
                entry(Status.SYSTEM_FAILED, () -> {
                    throw new IllegalArgumentException("처리 중인 결제 건이 존재합니다.");
                })
        );

        public static void handleExistingPayment(Status status) {
            if (statusHandlers.containsKey(status)){
                statusHandlers.get(status).run();
            }
        }
    }
}
