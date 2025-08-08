package com.loopers.application.payment;


import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.stock.StockService;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PaymentFacade {

    private final UserService userService;
    private final PointService pointService;
    private final PaymentService paymentService;
    private final StockService stockService;
    private final OrderService orderService;

    public PaymentFacade(UserService userService, PointService pointService, PaymentService paymentService, StockService stockService, OrderService orderService) {
        this.userService = userService;
        this.pointService = pointService;
        this.paymentService = paymentService;
        this.stockService = stockService;
        this.orderService = orderService;
    }

    public void payment(String userId, PaymentCommand.Pay command){
        try{
            Long userPkId = userService.findUserPkId(userId);
            // 포인트 차감
            pointService.deduct(
                    PointCommand.Deduct.of(userPkId, command.paymentAmount())
            );
            // 주문 이력을 확인
            // TODO. 주문 상태를 확인해서 Lock
            // 다른 사람이 주문을 하러 못 들어옴. -> 이것도 맞는데 Payment 에서 "주문"으로 Lock 을 거는게 맞나. 여기서만 사용하려고 생성?

            // 결제 이력 저장
            // 포인트가 차감되더라도 결제 이력이 있으면 다시 롤백됨.
            paymentService.save(Payment.of(
                    command.orderId(), Payment.Type.POINT, command.paymentAmount()
            ));
        } catch (DataIntegrityViolationException e) {
            log.error("결제 처리 오류 ", e);
            throw new CoreException(ErrorType.CONFLICT, "이미 처리된 주문입니다.");
        } catch (RuntimeException e){
            // TODO. 재고 복구
            // Payment 에서 Duplicate 가 발생하는 경우엔 Order 를 늘리면 안되나?
            Map<Long, Long> orderLines = orderService.getOrderedProductQuantity(command.orderId());
            CompletableFuture.runAsync(() -> stockService.increaseStock(orderLines));
        }
    }
}
