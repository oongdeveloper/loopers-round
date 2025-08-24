package com.loopers.domain.payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findByKey(String key);

    List<Payment> findByStatus(Payment.Status status);
}
