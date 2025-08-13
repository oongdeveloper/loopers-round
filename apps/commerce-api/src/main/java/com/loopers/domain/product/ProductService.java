package com.loopers.domain.product;

import com.loopers.domain.product.projections.ProductListProjection;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;

    public ProductService(ProductRepository productRepository, ProductSkuRepository productSkuRepository) {
        this.productRepository = productRepository;
        this.productSkuRepository = productSkuRepository;
    }

    public Page<ProductListProjection> getProductList(Long brandId,
                                                     String sort,
                                                     Pageable pageable) {
        return productRepository.findByBrandId(brandId, sort, pageable);
    }

    public Product getProductDetail(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 상품입니다."));
    }

    public List<Product> getProuctListByIds(Collection<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return List.of();
        }
        return productRepository.findProductsBySkuIds(skuIds);
    }
}
