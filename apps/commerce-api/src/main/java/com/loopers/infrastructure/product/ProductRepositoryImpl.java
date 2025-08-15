package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.projections.ProductListProjection;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import com.loopers.domain.product.projections.ProductListProjectionV3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<ProductListProjection> findByBrandId(Long brandId, String sort, Pageable pageable) {
        return jpaRepository.findByBrandId(brandId, sort, pageable);
    }

    @Override
    public List<ProductListProjectionV2> findByBrandIdBySortType(Long brandId, String sort, Pageable pageable) {
        return jpaRepository.findByBrandIdBySortTypeV2(brandId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), getSortType(sort)));
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }

    @Override
    public List<ProductListProjectionV3> findByBrandIdV3(Long brandId, String sort, Pageable pageable) {
        return jpaRepository.findByBrandIdV3(brandId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), getSortType(sort)));
    }

    public Sort getSortType(String sortType) {
        Sort sort;

        // sortType 문자열에 따라 Sort 객체를 동적으로 생성합니다.
        switch (sortType) {
            case "LATEST":
                sort = Sort.by(Sort.Direction.DESC, "created_at");
                break;
            case "PRICE_ASC":
                sort = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "LIKES_DESC":
                sort = Sort.by(Sort.Direction.DESC, "like_count");
                break;
            default:
                sort = Sort.by(Sort.Direction.DESC, "created_at");
                break;
        }
        return sort;
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return jpaRepository.findById(productId);
    }

    @Override
    public List<Product> findProductsBySkuIds(Collection<Long> skuIds) {
        return jpaRepository.findProductsBySkuIds(skuIds);
    }
}
