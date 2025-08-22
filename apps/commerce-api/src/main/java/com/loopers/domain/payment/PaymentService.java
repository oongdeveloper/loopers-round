package com.loopers.domain.payment;

import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final RedisCacheWrapper redisCacheWrapper;
    private static final String PAYMENT_REDIS_KEY_PREFIX = "payment:";

    public PaymentService(PaymentRepository paymentRepository, RedisCacheWrapper redisCacheWrapper) {
        this.paymentRepository = paymentRepository;
        this.redisCacheWrapper = redisCacheWrapper;
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findByKey(String key) {
        return paymentRepository.findByKey(key);
    }

    public PaymentKeyGenerator.UniquePaymentKey generateKey(Long userId, Long orderId) {
        String redisKey = String.format("%s:%s:%s", PAYMENT_REDIS_KEY_PREFIX, String.valueOf(userId), String.valueOf(orderId));
        PaymentKeyGenerator.UniquePaymentKey key = PaymentKeyGenerator.generate();
        redisCacheWrapper.set(redisKey, key.getKey(),15L, TimeUnit.MINUTES);
        return key;
    }

    public String findIdempotencyKey(PaymentCommand command) {
        String redisKey = String.format("%s:%s:%s", PAYMENT_REDIS_KEY_PREFIX, command.getUserId(), command.getOrderId());
        String storedKey = redisCacheWrapper.get(redisKey, String.class);
        if (storedKey == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 결제키입니다.");
        }
        return storedKey;
    }

    public List<Payment> findByStatus(Payment.Status status) {
        return paymentRepository.findByStatus(status);
    }
}
