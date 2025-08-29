package com.loopers.application.order;

import com.loopers.domain.payment.PaymentEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventListener {
    private final OrderFacade orderFacade;

    public OrderEventListener(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void handle(PaymentEvent.Completed event){
        orderFacade.completed(event.orderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void handle(PaymentEvent.Canceled event){
        orderFacade.failed(event.orderId());
    }
}
