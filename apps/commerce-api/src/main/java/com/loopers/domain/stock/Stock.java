package com.loopers.domain.stock;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock", uniqueConstraints = {
        @UniqueConstraint(name = "uq_product_sku_id", columnNames = "ref_product_sku_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stock extends BaseEntity {

    @Column(name = "ref_product_sku_id", nullable = false)
    Long productSkuId;

    @Column(name = "quantity", nullable = false)
    Long quantity;

    private Stock(Long productSkuId, Long quantity) {
        this.productSkuId = productSkuId;
        this.quantity = quantity;
    }

    private void validate(Long productSkuId, Long quantity){
        if (productSkuId == null || quantity == null || quantity < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST,"제품 옵션 ID 는 필수 값입니다.");
        }

        if(quantity == null || quantity < 0){
            throw new CoreException(ErrorType.BAD_REQUEST,"수량은 비어있거나 음수일 수 없습니다.");
        }
    }

    public static Stock from(Long productSkuId, Long quantity) {
        return new Stock(productSkuId, quantity);
    }

    public void increaseStock(long quantity){
        if (quantity <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST,"증가시킬 재고 수량은 0보다 커야 합니다.");
        }
        this.quantity += quantity;
    }

    public void decreaseStock(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("감소시킬 재고 수량은 0보다 커야 합니다.");
        }
        if (this.quantity - quantity < 0) {
            throw  new IllegalArgumentException("재고가 부족하여 감소시킬 수 없습니다.");
        }
        this.quantity -= quantity;
    }
}
