package com.loopers.application.stock;

import com.loopers.domain.stock.StockCommand;
import com.loopers.domain.stock.StockService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockFacade {
    private final StockService stockService;
    private final ApplicationEventPublisher eventPublisher;

    public StockFacade(StockService stockService, ApplicationEventPublisher eventPublisher) {
        this.stockService = stockService;
        this.eventPublisher = eventPublisher;
    }

    public void reduce(StockCommand.Reduce command){
        stockService.reduceStock(command.items());
    }

    @Retry(name = "internalTaskRetry", fallbackMethod = "fallbackFailedRestore")
    public void restore(StockCommand.Restore command){
        stockService.restoreStock(command.items());
    }

    public void fallbackFailedRestore(StockCommand.Restore command, RuntimeException e){
        log.error("재고 복구 시, 오류가 발생했습니다. ", e);
        eventPublisher.publishEvent(StockRestoreFailedEvent.of(command.orderId(), command.items(), "재고 복구 실패"));
    }
}
