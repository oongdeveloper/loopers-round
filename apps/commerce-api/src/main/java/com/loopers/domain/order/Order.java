package com.loopers.domain.order;

import com.loopers.domain.common.AggregateRoot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends AggregateRoot {

    @Column(name = "ref_user_id", nullable = false)
    Long userId;

//    @Column(name = "total_order_price", nullable = false)
//    BigDecimal totalOrderPrice;

    @Column(name = "original_total_price", nullable = true, precision = 12, scale = 2)
    private BigDecimal originalTotalPrice;

    @Column(name = "discount_amount", nullable = true, precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "final_total_price", nullable = true, precision = 12, scale = 2)
    private BigDecimal finalTotalPrice;

    @Column(name = "ref_user_coupon_id", nullable = true)
    Long couponId;

    // TODO. Enum 처리 해야됨
//    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_v2")
    Status statusV2;

    @Embedded
    OrderLines lines;

    private Order(Long userId, String status){
        this.userId = userId;
        this.status = status;
        this.statusV2 = Status.valueOf(status);
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
        registerEvent(OrderEvent.Created.from(this));
    }

    public void updateStatus(Status status) {
        this.statusV2 = status;
    }

    public void complete(){
        if(this.statusV2.equals(Status.COMPLETED)) return;

        this.statusV2 = Status.COMPLETED;
        registerEvent(OrderEvent.Completed.from(this));
    }

    public void fail(){
        if(this.statusV2.equals(Status.FAILED)) return;

        this.statusV2 = Status.FAILED;
        registerEvent(OrderEvent.Canceled.from(this));
    }

    public enum Status{
        NEW, PENDING, COMPLETED, FAILED
    }
}
