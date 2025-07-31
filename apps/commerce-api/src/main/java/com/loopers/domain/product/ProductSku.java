package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_sku")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductSku extends BaseEntity {
    @Column(name = "ref_product_definition_id", nullable = false)
    private Long productDefinitionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SkuStatus status;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    @OneToMany(mappedBy = "productSku")
    private List<SkuOption> skuOptions = new ArrayList<>();

    @Getter
    public enum SkuStatus{
        AVAILABLE("AVAILABLE", "구매 가능"),
        OUT_OF_STOCK("OUT_OF_STOCK", "품절"),
        PRE_ORDER("PRE_ORDER", "예약 판매"),
        RESTOCKING("RESTOCKING", "재입고 예정"),
        DISCONTINUED("DISCONTINUED", "판매 중지")
        ;

        private final String code;
        private final String description;

        SkuStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }


    public static ProductSku from(List<SkuOption> skuOptions, String imageUrl, BigDecimal unitPrice, SkuStatus status, Long productDefinitionId){
        return new ProductSku(
                skuOptions,
                imageUrl,
                unitPrice,
                status,
                productDefinitionId
        );
    }

    private ProductSku(List<SkuOption> skuOptions, String imageUrl, BigDecimal unitPrice, SkuStatus status, Long productDefinitionId) {
        validate(imageUrl, unitPrice);
        this.skuOptions = skuOptions;
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        this.status = status;
        this.productDefinitionId = productDefinitionId;
    }

    private void validate(String imageUrl, BigDecimal unitPrice){
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 가격은 0 이하일 수 없습니다.");

        if (unitPrice.compareTo(BigDecimal.valueOf(10_000_000_000L)) >= 0)
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 가격은 최대 10억 미만으로만 설정 가능합니다.");

        if (imageUrl == null) return;
        try{
            new URL(imageUrl).toURI();
        } catch (MalformedURLException | URISyntaxException e){
            throw new CoreException(ErrorType.BAD_REQUEST, "올바른 URL 형식이 아닙니다.");
        }
    }

}

