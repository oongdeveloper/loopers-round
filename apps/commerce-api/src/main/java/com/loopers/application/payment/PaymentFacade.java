package com.loopers.application.payment;


import com.loopers.application.payment.method.PaymentMethodExecutor;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.pg.PgGatewayService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.loopers.domain.pg.PgSourceInfo.PgTransactionDetail;

@Service
@Slf4j
public class PaymentFacade {
    private final PaymentManager paymentManager;
    private final PaymentMethodExecutor paymentMethodExecutor;
    private final PostPaymentHandler postPaymentHandler;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PgGatewayService pgGatewayService;

    public PaymentFacade(PaymentManager paymentManager, PaymentMethodExecutor paymentMethodExecutor, PostPaymentHandler postPaymentHandler, OrderService orderService, PaymentService paymentService, PgGatewayService pgGatewayService) {
        this.paymentManager = paymentManager;
        this.paymentMethodExecutor = paymentMethodExecutor;
        this.postPaymentHandler = postPaymentHandler;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.pgGatewayService = pgGatewayService;
    }

    public PaymentResult pay(PaymentCommand command){
        try{
            // 결제키 확인
            if(!command.getIdempotencyKey().equals(paymentService.findIdempotencyKey(command)))
                throw new CoreException(ErrorType.BAD_REQUEST, "잘못된 결제 키 요청입니다.");

            Order order = orderService.findByIdAndUserId(command.getOrderId(), command.getUserId())
                    .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 주문에 대한 결제 요청입니다."));

            if (command.getAmount().compareTo(order.getFinalTotalPrice()) != 0)
                throw new CoreException(ErrorType.BAD_REQUEST, "잘못된 결제 요청입니다.");
            Payment payment = paymentManager.create(command);

            // 결제 요청
            PaymentResult result = paymentMethodExecutor.execute(command);
            Payment paidPayment = paymentManager.find(command);
            postPaymentHandler.postPayment(paidPayment);

            return result;
        } catch (Exception e) {
            log.error("결제 처리 오류 ", e);
            throw new CoreException(ErrorType.INTERNAL_ERROR, "결제 처리 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public void postPg(PgTransactionDetail req){
        try{
            // 상태 API 를 찔러서 받은 결과와 비교
            PgTransactionDetail response = pgGatewayService.checkTransactionStatus(req.transactionKey());
            if (!req.status().equals(response.status()) || req.amount().compareTo(response.amount()) != 0){
                throw new CoreException(ErrorType.BAD_REQUEST, "잘못된 결제 Callback 입니다. " + req.transactionKey());
            }

            // key 로 주문 테이블 찾아서 비교 후 업데이트
            Payment payment = paymentService.findByKey(req.orderId())
                    .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 결제 이력입니다. Callback 확인 필요 " + req.transactionKey()));

            if (req.amount().compareTo(payment.getAmount()) != 0) {
                throw new CoreException(ErrorType.BAD_REQUEST, "결제 정보가 일치하지 않습니다. Callback 확인 필요 " + req.transactionKey());
            }

            if(req.status().equals(PgTransactionDetail.Status.SUCCESS)){
                payment.updateStatus(Payment.Status.COMPLETED);
            } else if(req.status().equals(PgTransactionDetail.Status.FAILED)) {
                payment.updateStatus(Payment.Status.FAILED);
                payment.setReason(req.reason());
            }
            postPaymentHandler.postPayment(payment);
        } catch (RuntimeException e){
            log.error("결제 콜백 오류 발생.", e);
            throw e;
        }
    }

    public String generateKey(Long userId, Long orderId){
        return paymentService.generateKey(userId, orderId).getKey();
    }
}
