package com.loopers.domain.order;

import com.loopers.application.order.OrderCreateCommand;
import com.loopers.application.order.OrderInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(OrderCreateCommand request) {
        Order order = Order.of(request.userId(), "PENDING");

        request.items().forEach(itemDto -> {
            OrderItem orderItem = OrderItem.of(
                    itemDto.productSkuId(),
                    itemDto.productCatalogId(),
                    itemDto.quantity(),
                    itemDto.unitPrice(),
                    itemDto.productName()
            );
            order.addOrderItem(orderItem);
        });

        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    public Page<OrderInfo.OrderResponseInfo> getOrderList(Long userId, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByUserId(userId, pageable);
        return ordersPage.map(OrderInfo.OrderResponseInfo::from);
    }

    public OrderInfo.OrderDetailInfo getOrderDetail(Long orderId) {
        Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "주문을 찾을 수 없습니다. orderId: " + orderId));

        return OrderInfo.OrderDetailInfo.from(order);
    }

    public void save(Order order){
        orderRepository.save(order);
    }
}
