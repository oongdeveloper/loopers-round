package com.loopers.domain.catalog;


import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Entity
@Table(name = "brand")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {
    @Column(name = "brand_name", nullable = false, length = 100, unique = true)
    String brandName;

    @Column(name = "logo_url", length = 2048)
    String logoUrl;

    private Brand(String brandName, String logoUrl) {
        validate(brandName, logoUrl);

        this.brandName = brandName;
        this.logoUrl = logoUrl;
    }

    public static Brand from(String brandName, String logoUrl) {
        return new Brand(brandName, logoUrl);
    }

    private void validate(String brandName, String logoUrl) {
        if (brandName == null || brandName.length() > 100)
            throw new CoreException(ErrorType.BAD_REQUEST, "브랜드 이름은 100자 이내여야 합니다.");

        if (logoUrl == null) return;
        try{
            new URL(logoUrl).toURI();
        } catch (MalformedURLException | URISyntaxException e){
            throw new CoreException(ErrorType.BAD_REQUEST, "올바른 URL 형식이 아닙니다.");
        }
    }
}
