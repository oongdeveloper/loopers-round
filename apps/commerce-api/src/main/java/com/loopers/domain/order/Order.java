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

//    @Column(name = "total_order_price", nullable = false)
//    BigDecimal totalOrderPrice;

    @Column(name = "original_total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal originalTotalPrice;

    @Column(name = "discount_amount", nullable = true, precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "final_total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal finalTotalPrice;

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
        this.originalTotalPrice = this.lines.calculateTotalAmount();
    }

    public void updateFinalTotalPrice(BigDecimal finalTotalPrice){
        this.finalTotalPrice = finalTotalPrice;
    }

    public void created(){
        this.status = "CREATED";
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
