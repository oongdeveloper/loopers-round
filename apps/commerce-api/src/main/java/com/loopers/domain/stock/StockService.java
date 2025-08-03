package com.loopers.domain.stock;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Optional<Stock> findStock(Long id) {
        return stockRepository.findByProductSkuId(id);
    }

    public List<Stock> findBySkuIds(Collection<Long> skuIds){
        if (skuIds == null || skuIds.isEmpty()) {
            return List.of(); // 빈 목록이 들어오면 빈 리스트 반환
        }
        return stockRepository.findBySkuIdIn(skuIds);
    }

    public void decreaseStock(Long id, Long quantity){
        stockRepository.findByProductSkuId(id)
                .ifPresentOrElse(
                        stock1 -> {stock1.decreaseStock(quantity);},
                        () -> {
                            throw new CoreException(ErrorType.BAD_REQUEST, "재고가 존재하지 않는 상품입니다.");
                        }
                );
    }

    public void decreaseStock(List<Stock> skuIds, Map<Long,Long> requestMap){
        skuIds.forEach(sku ->{
                    sku.decreaseStock(requestMap.get(sku.productSkuId));
                }
        );
    }
}
