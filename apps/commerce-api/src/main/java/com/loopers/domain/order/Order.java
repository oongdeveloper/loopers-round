package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {

    @Column(name = "ref_user_id", nullable = false)
    String userId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total_order_price", nullable = false)
    BigDecimal totalOrderPrice;

    @Column(name = "status", nullable = false)
    String status;

    @Embedded
    OrderLines lines;

    private Order(String userId, String status){
        this.userId = userId;
        this.status = status;
    }

    public static Order of(String userId, String status){
        return new Order(userId, status);
    }

    public static Order create(OrderFactory.OrderCommand.Create createCommand) {
        Order createdOrder = new Order(createCommand.userId(), "NEW");
        createdOrder.lines = OrderLines.empty();

        createCommand.items().forEach(itemCommand -> {
            createdOrder.lines.add(OrderLine.from(itemCommand));
        });
        return createdOrder;
    }

    public void addOrderItem(OrderItem item) {
        if (item == null) {
            throw new CoreException(ErrorType.BAD_REQUEST,"OrderItem은 null이 될 수 없습니다.");
        }
        this.orderItems.add(item);
        item.setOrder(this);
        calculateTotalPrice();
    }

    public void removeOrderItem(OrderItem item) {
        if (item == null) {
            return;
        }

        boolean removed = this.orderItems.remove(item);
        if (removed) {
            item.setOrder(null);
            calculateTotalPrice();
        }
    }

    public void calculateTotalPrice() {
        if (this.orderItems == null || this.orderItems.isEmpty()) {
            this.totalOrderPrice = BigDecimal.ZERO;
            return;
        }
        BigDecimal sum = this.orderItems.stream()
                .map(OrderItem::getTotalItemPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalOrderPrice = sum;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
