package com.loopers.infrastructure.stock;

import com.loopers.domain.stock.Stock;
import com.loopers.domain.stock.StockRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository jpaRepository;

    public StockRepositoryImpl(StockJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Stock> findByProductSkuId(Long id) {
        return jpaRepository.findByProductSkuId(id);
    }

    @Override
    public Stock save(Stock stock) {
        return jpaRepository.save(stock);
    }
}
