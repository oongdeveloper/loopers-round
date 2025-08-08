package com.loopers.domain.stock;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.support.error.InsufficientStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseStock(Map<Long,Long> requestMap){
        List<Stock> stocks = findBySkuIds(requestMap.keySet());
        stocks.forEach(stock ->{
                    stock.increaseStock(requestMap.get(stock.productSkuId));
                }
        );
    }
}
