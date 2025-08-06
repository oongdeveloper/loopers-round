package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
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
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    Order order;

    @Column(name = "product_sku_id", nullable = false)
    Long productSkuId;
    @Column(name = "product_catalog_id", nullable = false)
    Long productCatalogId;

    @Column(name = "quantity", nullable = false)
    Long quantity;
    @Column(name = "total_line_price", nullable = false)
    BigDecimal totalLinePrice;

    @Column(name = "order_line_product_name", nullable = false)
    String orderLineProductName;
    @Column(name = "order_line_price", nullable = false)
    BigDecimal orderLinePrice;

    public OrderLine(Long productSkuId, Long productCatalogId, Long quantity,
                     BigDecimal totalLinePrice, String orderLineProductName, BigDecimal orderLinePrice) {
        this.order = order;
        this.productSkuId = productSkuId;
        this.productCatalogId = productCatalogId;
        this.quantity = quantity;
        this.totalLinePrice = totalLinePrice;
        this.orderLineProductName = orderLineProductName;
        this.orderLinePrice = orderLinePrice;
    }

    public static OrderLine from(OrderFactory.OrderCommand.OrderItem item){
        return new OrderLine(
                item.productSkuId(),
                item.productCatalogId(),
                item.quantity(),
                item.orderLinePrice(),
                item.orderLineProuductName(),
                item.price()
        );
    }

    public boolean hasSameProduct(OrderLine other){
        return Objects.equals(this.productSkuId, other.productSkuId);
    }

}
