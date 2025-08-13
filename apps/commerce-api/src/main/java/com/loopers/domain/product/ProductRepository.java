package com.loopers.domain.product;

import com.loopers.domain.product.projections.ProductListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Page<ProductListProjection> findByBrandId(@Param("brandId") Long brandId,
                                              @Param("sort") String sort,
                                              Pageable pageable);
    Optional<Product> findById(Long productId);

    List<Product> findProductsBySkuIds(Collection<Long> skuIds);
}
