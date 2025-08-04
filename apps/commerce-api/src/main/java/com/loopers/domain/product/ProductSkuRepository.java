package com.loopers.domain.product;

import java.util.Collection;
import java.util.List;

public interface ProductSkuRepository {
    List<ProductSku> findByProductCatalogId(Long id);
    List<ProductSku> findByIdIn(Collection<Long> skuIds);

    ProductSku save(ProductSku entity);
}
