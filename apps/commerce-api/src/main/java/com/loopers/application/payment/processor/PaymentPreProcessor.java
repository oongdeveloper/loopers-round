package com.loopers.application.payment.processor;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.loopers.domain.payment.Payment.*;

@Slf4j
@Component
public class PaymentPreProcessor {
    private final PaymentService paymentService;

    public PaymentPreProcessor(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Transactional
    public void preprocess(PaymentCommand command) {
        log.info("주문번호 : {}, 멱등키 {}, 결제방식 {} 으로 결제 시도합니다.", command.getOrderId(), command.getIdempotencyKey(), command.getMethod());
        Payment payment = paymentService.findByKey(command.getIdempotencyKey())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "잘못된 결제 요청입니다. " + command.getIdempotencyKey()));
        payment.updateStatus(Status.PENDING);
    }
}
