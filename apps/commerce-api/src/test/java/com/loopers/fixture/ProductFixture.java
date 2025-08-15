package com.loopers.fixture;

import com.loopers.domain.product.Product;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import java.math.BigDecimal;

import static org.instancio.Select.field;

public class ProductFixture {

    public static ProductBuilder builder(){
        return new ProductBuilder();
    }

    public static ProductBuilder persistence(){
        return new ProductBuilder()
                .id(null);
    }

    public static class ProductBuilder{
        private final InstancioApi<Product> api;

        public ProductBuilder(){
            this.api = Instancio.of(Product.class);
        }

        public ProductBuilder id(Long id) {
            this.api.set(field(Product::getId), id);
            return this;
        }

        public ProductBuilder brandId(Long id) {
            this.api.set(field(Product::getBrandId), id);
            return this;
        }

        public ProductBuilder productName(String name) {
            this.api.set(field(Product::getProductName), name);
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.api.set(field(Product::getPrice), price);
            return this;
        }

        public ProductBuilder imageUrl(String imageUrl) {
            this.api.set(field(Product::getImageUrl), imageUrl);
            return this;
        }

        public Product build(){
            return this.api.create();
        }
    }
}
