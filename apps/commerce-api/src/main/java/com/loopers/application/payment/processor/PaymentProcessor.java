package com.loopers.application.payment.processor;

import com.loopers.application.payment.method.PaymentExecutor;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.PaymentCommand;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessor {
    private final PaymentPreProcessor preProcessor;
    private final PaymentPostProcessor postProcessor;
    private final PaymentExecutor paymentExecutor;

    public PaymentProcessor(PaymentPreProcessor preProcessor, PaymentPostProcessor postProcessor, PaymentExecutor paymentExecutor) {
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
        this.paymentExecutor = paymentExecutor;
    }

    public PaymentResult process(PaymentCommand command) {
        preProcessor.preprocess(command);
        PaymentResult result = paymentExecutor.execute(command);
        postProcessor.postprocess(result);

        return result;
    }
}
