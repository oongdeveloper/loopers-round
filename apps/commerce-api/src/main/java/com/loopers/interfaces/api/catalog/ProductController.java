package com.loopers.interfaces.api.catalog;

import com.loopers.PaginationRequest;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductQuery;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductV1ApiSpec{
    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Override
    @GetMapping("/api/v1/products")
    public ApiResponse<?> getList(Long brandId, String sortType, PaginationRequest paginationRequest) {
        Page<ProductInfo.DataList> result = productFacade.getProductList(ProductQuery.Summary.of(brandId, sortType, paginationRequest.toPageable()));
        return ApiResponse.success(result);
    }

    public record Detail(Long productId){
        public static ProductQuery.Detail of(Long productId){
            return new ProductQuery.Detail(productId);
        }

    }

    @Override
    @GetMapping("/api/v1/products/{productId}")
    public ApiResponse<?> getDetail(@RequestHeader("X-USER-ID") Long userId, @PathVariable("productId") Long productId) {
        ProductInfo.DataDetail result = productFacade.getProductDetail(userId, ProductQuery.Detail.of(productId));
        return ApiResponse.success(result);
    }
}
