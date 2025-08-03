package com.loopers.infrastructure.stock;

import com.loopers.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductSkuId(Long id);
    List<Stock> findByProductSkuIdIn(Collection<Long> skuIds);
}
