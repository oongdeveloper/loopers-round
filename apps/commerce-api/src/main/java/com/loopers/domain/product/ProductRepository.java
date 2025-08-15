package com.loopers.domain.product;

import com.loopers.domain.product.projections.ProductListProjection;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import com.loopers.domain.product.projections.ProductListProjectionV3;
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

    List<ProductListProjectionV3> findByBrandIdV3(@Param("brandId") Long brandId,
                                                  @Param("sort") String sort,
                                                  Pageable pageable);

    List<ProductListProjectionV2> findByBrandIdBySortType(Long brandId, String sort, Pageable pageable);

    Long count();

    Optional<Product> findById(Long productId);

    List<Product> findProductsBySkuIds(Collection<Long> skuIds);
}
