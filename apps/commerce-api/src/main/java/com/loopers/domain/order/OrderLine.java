package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_line")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    Order order;

    @Column(name = "ref_product_sku_id", nullable = false)
    Long productSkuId;

    @Column(name = "quantity", nullable = false)
    Long quantity;
    @Column(name = "total_line_price", nullable = false)
    BigDecimal totalLinePrice;

    @Column(name = "order_line_product_name", nullable = false)
    String orderLineProductName;
    @Column(name = "order_line_price", nullable = false)
    BigDecimal orderLinePrice;

    private OrderLine(Long productSkuId, Long quantity,
                     String orderLineProductName, BigDecimal orderLinePrice) {

        validate(quantity, orderLinePrice);

        this.productSkuId = productSkuId;
        this.quantity = quantity;
        this.orderLineProductName = orderLineProductName;
        this.orderLinePrice = orderLinePrice;
        this.totalLinePrice = calculateTotalLinePrice();
    }

    public static OrderLine create(Long productSkuId, Long quantity,
                     String orderLineProductName, BigDecimal orderLinePrice) {
        return new OrderLine(productSkuId, quantity, orderLineProductName, orderLinePrice);
    }

    public boolean hasSameProduct(OrderLine other){
        return Objects.equals(this.productSkuId, other.productSkuId);
    }

    public void validate(Long quantity, BigDecimal orderItemUnitPrice){
        if(quantity == null || quantity < 0){
            throw new CoreException(ErrorType.BAD_REQUEST, "수량은 0보다 작을 수 없습니다.");
        }

        if (orderItemUnitPrice == null || orderItemUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "단가는 0보다 작을 수 없습니다.");
        }
    }

    public BigDecimal calculateTotalLinePrice() {
        if (this.quantity == null || this.orderLinePrice == null) {
            this.totalLinePrice = BigDecimal.ZERO; // 또는 예외 처리
            return this.totalLinePrice;
        }

        this.totalLinePrice = this.orderLinePrice.multiply(BigDecimal.valueOf(this.quantity));
        return this.totalLinePrice;
    }

    protected void setOrder(Order order){
        this.order = order;
    }

}
