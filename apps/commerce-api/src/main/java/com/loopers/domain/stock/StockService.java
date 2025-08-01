package com.loopers.domain.stock;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

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

    public void decreaseStock(Long id, Long quantity){
        stockRepository.findByProductSkuId(id)
                .ifPresentOrElse(
                        stock1 -> {stock1.decreaseStock(quantity);},
                        () -> {
                            throw new CoreException(ErrorType.BAD_REQUEST, "재고가 존재하지 않는 상품입니다.");
                        }
                );
    }
}
