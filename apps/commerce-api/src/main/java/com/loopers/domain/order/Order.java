package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {

    @Column(name = "ref_user_id", nullable = false)
    Long userId;

    @Column(name = "total_order_price", nullable = false)
    BigDecimal totalOrderPrice;

    // TODO. Enum 처리 해야됨
//    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    String status;

    @Embedded
    OrderLines lines;

    private Order(Long userId, String status){
        this.userId = userId;
        this.status = status;
    }

    public static Order create(Long userId) {
        Order createdOrder = new Order(userId, "NEW");
        createdOrder.lines = OrderLines.empty();
        return createdOrder;
    }

    public void addOrderLine(OrderLine line) {
        lines.add(line);
    }

    public void calculateTotalPrice() {
        this.totalOrderPrice = this.lines.calculateTotalAmount();
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
