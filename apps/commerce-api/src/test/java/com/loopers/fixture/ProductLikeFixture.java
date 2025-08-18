package com.loopers.fixture;

import com.loopers.domain.like.ProductLike;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class ProductLikeFixture {
    public static ProductLikeBuilder builder(){
        return new ProductLikeBuilder();
    }

    public static ProductLikeBuilder persistence(){
        return new ProductLikeBuilder()
                .productId(null);
    }

    public static class ProductLikeBuilder{
        private final InstancioApi<ProductLike> api;

        public ProductLikeBuilder(){
            this.api = Instancio.of(ProductLike.class);
        }

        public ProductLikeBuilder productId(Long id) {
            this.api.set(field(ProductLike::getProductId), id);
            return this;
        }

        public ProductLikeBuilder count(Long count) {
            this.api.set(field(ProductLike::getLikeCount), count);
            return this;
        }

        public ProductLike build(){
            return this.api.create();
        }
    }
}
