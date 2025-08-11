package com.loopers.fixture;

import com.loopers.domain.catalog.Brand;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class BrandFixture {
    public static BrandBuilder builder(){
        return new BrandBuilder();
    }

    public static BrandBuilder persistence(){
        return new BrandBuilder()
                .id(null);
    }

    public static class BrandBuilder{
        private final InstancioApi<Brand> api;

        public BrandBuilder(){
            this.api = Instancio.of(Brand.class);
        }

        public BrandBuilder id(Long id) {
            this.api.set(field(Brand::getId), id);
            return this;
        }

        public BrandBuilder brandName(String name) {
            this.api.set(field(Brand::getBrandName), name);
            return this;
        }

        public BrandBuilder logoUrl(String logoUrl) {
            this.api.set(field(Brand::getLogoUrl), logoUrl);
            return this;
        }

        public Brand build(){
            return this.api.create();
        }
    }
}
