package com.loopers.domain.stock;

import java.util.Optional;

public interface StockRepository {
    Optional<Stock> findByProductSkuId(Long id);

    Stock save(Stock stock);
}
