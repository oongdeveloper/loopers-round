package com.loopers.domain.stock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StockRepository {
    Optional<Stock> findByProductSkuId(Long id);
    Stock save(Stock stock);
    List<Stock> findBySkuIdIn(Collection<Long> skuIds);
}
