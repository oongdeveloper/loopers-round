package com.loopers.application.product;

import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.stock.StockEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductEventListener {
    private final ProductFacade productFacade;

    public ProductEventListener(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(StockEvent.OnSale event){
        productFacade.cacheEvict(ProductQuery.Detail.of(event.productId()));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(StockEvent.OutOfStock event){
        productFacade.cacheEvict(ProductQuery.Detail.of(event.productId()));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(LikeEvent.Liked event){
        productFacade.cacheEvict(ProductQuery.Detail.of(event.productId()));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(LikeEvent.UnLiked event){
        productFacade.cacheEvict(ProductQuery.Detail.of(event.productId()));
    }
}
