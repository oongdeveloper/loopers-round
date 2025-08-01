package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    Order order;

    @Column(name = "product_sku_id", nullable = false)
    Long productSkuId;
    @Column(name = "product_catalog_id", nullable = false)
    Long productCatalogId;

    @Column(name = "quantity", nullable = false)
    Long quantity;
    @Column(name = "total_item_price", nullable = false)
    BigDecimal totalItemPrice;

    @Column(name = "order_item_product_name", nullable = false)
    String orderItemProductName;
    @Column(name = "order_item_unit_price", nullable = false)
    BigDecimal orderItemUnitPrice;

    private OrderItem(Long productSkuId, Long productCatalogId, Long quantity,
                     BigDecimal orderItemUnitPrice, String orderItemProductName) {

        validate(quantity, orderItemUnitPrice);

        this.productSkuId = productSkuId;
        this.productCatalogId = productCatalogId;
        this.quantity = quantity;
        this.orderItemUnitPrice = orderItemUnitPrice;
        this.orderItemProductName = orderItemProductName;

        calculateTotalItemPrice();
    }

    public static OrderItem of(Long productSkuId, Long productCatalogId, Long quantity,
                               BigDecimal orderItemUnitPrice, String orderItemProductName) {
        return new OrderItem(productSkuId, productCatalogId, quantity,
                            orderItemUnitPrice, orderItemProductName);
    }

    public void validate(Long quantity, BigDecimal orderItemUnitPrice){
        if(quantity == null || quantity < 0){
            throw new CoreException(ErrorType.BAD_REQUEST, "수량은 0보다 작을 수 없습니다.");
        }

        if (orderItemUnitPrice == null || orderItemUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "단가는 0보다 작을 수 없습니다.");
        }
    }

    public void updateQuantity(Long newQuantity) {
        if (newQuantity == null || newQuantity < 0) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
        this.quantity = newQuantity;
        calculateTotalItemPrice();
    }

    public void updateUnitPrice(BigDecimal newUnitPrice) {
        if (newUnitPrice == null || newUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("단가는 0보다 작을 수 없습니다.");
        }
        this.orderItemUnitPrice = newUnitPrice;
        calculateTotalItemPrice();
    }

    private void calculateTotalItemPrice() {
        if (this.quantity == null || this.orderItemUnitPrice == null) {
            this.totalItemPrice = BigDecimal.ZERO; // 또는 예외 처리
            return;
        }

        this.totalItemPrice = this.orderItemUnitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    protected void setOrder(Order order){
        this.order = order;
    }
}
