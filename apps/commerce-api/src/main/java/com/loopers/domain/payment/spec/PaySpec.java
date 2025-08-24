package com.loopers.domain.payment.spec;

import com.loopers.domain.payment.Payment;

public interface PaySpec {
    Payment.Method getMethod();
}
