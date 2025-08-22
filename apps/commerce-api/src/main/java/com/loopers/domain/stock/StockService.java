package com.loopers.domain.stock;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.support.error.InsufficientStockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock find(Long id) {
        return stockRepository.findByProductSkuId(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품 재고를 찾을 수 없습니다."));
    }

    public List<Stock> findBySkuIds(Collection<Long> skuIds){
        if (skuIds == null || skuIds.isEmpty()) {
            return List.of();
        }
        return stockRepository.findBySkuIdIn(skuIds);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(Map<Long,Long> requestMap){
        List<Stock> stocks = findBySkuIds(requestMap.keySet());
//        stocks.forEach(stock ->{
//                    stock.decreaseStock(requestMap.get(stock.productSkuId));
//                }
//        );

        List<Long> failedSkuIds = new ArrayList<>();
        for (Stock stock : stocks) {
            try {
                stock.decreaseStock(requestMap.get(stock.productSkuId));
            } catch (IllegalArgumentException e) {
                failedSkuIds.add(stock.productSkuId);
            }
        }

        if (!failedSkuIds.isEmpty()) {
            throw new InsufficientStockException(failedSkuIds);
        }
    }

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000, multiplier = 2)
    )

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional
    public void restoreStock(Map<Long,Long> requestMap){
        List<Stock> stocks = findBySkuIds(requestMap.keySet());
        stocks.forEach(stock ->{
                    stock.restoreStock(requestMap.get(stock.productSkuId));
                }
        );
    }

    @Recover
    public void recover(RuntimeException e, Map<Long,Long> requestMap) {
        log.error("모든 재시도 실패! 복구 메서드 실행.");
        log.error("예외 메시지: {}", e.getMessage());
        log.error("재고 복구 실패.  {}", requestMap.toString());
    }
}
