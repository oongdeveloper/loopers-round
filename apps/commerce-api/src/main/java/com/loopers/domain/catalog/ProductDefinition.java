package com.loopers.domain.catalog;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Entity
@Table(name = "product_definition")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDefinition extends BaseEntity {

    @Column(name = "ref_brand_id", nullable = false)
    private Long brandId;

    @Column(name = "product_name", nullable = false, length = 250)
    private String productName;

    // TODO. 설계서 반영
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    private ProductDefinition(Long brandId, String productName, BigDecimal basePrice, String imageUrl, String description) {
        validate(brandId, productName, basePrice, imageUrl);

        this.brandId = brandId;
        this.productName = productName;
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static ProductDefinition from (Long brandId, String productName, BigDecimal basePrice, String imageUrl, String description){
        return new ProductDefinition(
                brandId, productName, basePrice, imageUrl, description
        );
    }

    private void validate(Long brandId, String productName, BigDecimal basePrice, String imageUrl){
        if (brandId == null)
            throw new CoreException(ErrorType.BAD_REQUEST, "Brand 정보가 존재하지 않습니다.");

        if (productName == null || productName.length() > 100)
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
