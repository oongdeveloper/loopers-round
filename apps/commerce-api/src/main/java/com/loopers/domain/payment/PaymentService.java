package com.loopers.domain.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
