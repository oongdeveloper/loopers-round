package com.loopers.domain.stock;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
            return List.of(); // 빈 목록이 들어오면 빈 리스트 반환
        }
        return stockRepository.findBySkuIdIn(skuIds);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(Map<Long,Long> requestMap){
        List<Stock> stocks = findBySkuIds(requestMap.keySet());
        stocks.forEach(stock ->{
                    stock.decreaseStock(requestMap.get(stock.productSkuId));
                }
        );
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
