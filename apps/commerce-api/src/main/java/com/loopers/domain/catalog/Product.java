package com.loopers.domain.catalog;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;

@Entity
@Table(name = "product_catalog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Product extends BaseEntity {

    @Column(name = "ref_brand_id", nullable = false)
    private Long brandId;

    @Column(name = "product_name", nullable = false, length = 250)
    private String productName;

    // TODO. 설계서 반영
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "published_at", updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private ZonedDateTime publishedAt;

    private Product(Long brandId, String productName, BigDecimal basePrice, String imageUrl, String description) {
        validate(brandId, productName, basePrice, imageUrl);

        this.brandId = brandId;
        this.productName = productName;
        this.price = basePrice;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static Product from (Long brandId, String productName, BigDecimal basePrice, String imageUrl, String description){
        return new Product(
                brandId, productName, basePrice, imageUrl, description
        );
    }

    private void validate(Long brandId, String productName, BigDecimal basePrice, String imageUrl){
        if (brandId == null)
            throw new CoreException(ErrorType.BAD_REQUEST, "Brand 정보가 존재하지 않습니다.");

        if (StringUtils.isBlank(productName) || productName.length() > 250)
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 이름은 250자 이내여야 합니다.");

        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 가격은 0 이하일 수 없습니다.");

        if (basePrice.compareTo(BigDecimal.valueOf(10_000_000_000L)) >= 0)
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 가격은 최대 10억 미만으로만 설정 가능합니다.");

        if (imageUrl == null) return;
        try{
            new URL(imageUrl).toURI();
        } catch (MalformedURLException | URISyntaxException e){
            throw new CoreException(ErrorType.BAD_REQUEST, "올바른 URL 형식이 아닙니다.");
        }
    }
}
