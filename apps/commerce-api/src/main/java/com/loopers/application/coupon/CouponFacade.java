package com.loopers.application.coupon;

import com.loopers.domain.coupons.issued.UserCouponCommand;
import com.loopers.domain.coupons.issued.UserCouponService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CouponFacade {

    private final UserCouponService userCouponService;
    private final ApplicationEventPublisher eventPublisher;

    public CouponFacade(UserCouponService userCouponService, ApplicationEventPublisher eventPublisher) {
        this.userCouponService = userCouponService;
        this.eventPublisher = eventPublisher;
    }

    public void use(UserCouponCommand.Use command){
        userCouponService.use(command);
    }

    @Retry(name = "internalTaskRetry", fallbackMethod = "fallbackFailedRollback")
    public void rollback(UserCouponCommand.Rollback command){
        userCouponService.rollbackCoupon(command);
    }

    public void fallbackFailedRollback(UserCouponCommand.Rollback command, RuntimeException e){
        log.error("쿠폰 롤백 시, 오류가 발생했습니다. ", e);
        eventPublisher.publishEvent(CouponRollbackFailedEvent.of(
                command.userId(), command.couponId(), "쿠폰 롤백 실패"
        ));
    }
}
