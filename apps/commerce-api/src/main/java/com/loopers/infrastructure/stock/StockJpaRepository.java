package com.loopers.infrastructure.stock;

import com.loopers.domain.stock.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductSkuId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Stock> findByProductSkuIdIn(Collection<Long> skuIds);
}
