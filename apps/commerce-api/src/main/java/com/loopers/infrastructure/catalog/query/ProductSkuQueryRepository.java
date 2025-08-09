package com.loopers.infrastructure.catalog.query;

import com.loopers.domain.product.ProductSku;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductSkuQueryRepository extends Repository<ProductSku, Long> {

    @Query("SELECT ps FROM ProductSku ps " +
            "LEFT JOIN FETCH ps.skuOptions so " +
            "LEFT JOIN FETCH so.optionName oname " +
            "LEFT JOIN FETCH so.optionValue ovalue " +
            "WHERE ps.productCatalogId = :productId")
    List<ProductSku> findById(@Param("productId") Long productId);

}


