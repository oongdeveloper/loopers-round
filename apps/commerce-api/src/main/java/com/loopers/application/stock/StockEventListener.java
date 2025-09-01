package com.loopers.application.stock;

import com.loopers.domain.order.OrderEvent;
import com.loopers.domain.stock.StockCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class StockEventListener {
    private final StockFacade stockFacade;

    public StockEventListener(StockFacade stockFacade) {
        this.stockFacade = stockFacade;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void handle(OrderEvent.Created event){
        stockFacade.reduce(StockCommand.Reduce.of(event.orderId(), event.orderedItems()));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderEvent.Canceled event){
        stockFacade.restore(StockCommand.Restore.of(event.orderId(), event.orderedItems()));
    }

}
