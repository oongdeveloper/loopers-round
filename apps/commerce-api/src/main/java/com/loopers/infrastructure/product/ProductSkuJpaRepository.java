package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ProductSkuJpaRepository extends JpaRepository<ProductSku, Long> {
    @Query("SELECT ps FROM ProductSku ps " +
            "LEFT JOIN FETCH ps.skuOptions so " +
            "LEFT JOIN FETCH so.optionName oname " +
            "LEFT JOIN FETCH so.optionValue ovalue " +
            "WHERE ps.productCatalogId = :productCatalogId")
    List<ProductSku> findByProductCatalogIdWithAllOptions(Long productCatalogId);

    List<ProductSku> findByIdIn(Collection<Long> ids);
}
