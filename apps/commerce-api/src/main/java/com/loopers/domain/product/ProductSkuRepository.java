package com.loopers.domain.product;

import java.util.List;

public interface ProductSkuRepository {
    List<ProductSku> findByProductCatalogId(Long id);
}
