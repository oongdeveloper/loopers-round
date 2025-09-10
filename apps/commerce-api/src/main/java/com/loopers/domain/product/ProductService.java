package com.loopers.domain.product;

import com.loopers.domain.product.projections.ProductListProjectionV2;
import com.loopers.domain.product.projections.ProductListProjectionV3;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductListProjectionV2> findByBrandIdBySortType(Long brandId, String sort, Pageable pageable){
        return productRepository.findByBrandIdBySortType(brandId, sort, pageable);
    };

    public List<ProductListProjectionV3> getProductListV3(Long brandId,
                                                          String sort,
                                                          Pageable pageable) {
        return productRepository.findByBrandIdV3(brandId, sort, pageable);
    }

    public Product getProductDetail(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 상품입니다."));
    }

    public List<Product> getProuctListBySkuIds(Collection<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return List.of();
        }
        return productRepository.findProductsBySkuIds(skuIds);
    }

    public Long count(){
        return productRepository.count();
    }

    public List<ProductListProjectionV2> getProductListByIds(Collection<Long> ids){
        return productRepository.findProductListByIds(ids);
    }
}
