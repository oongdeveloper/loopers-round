package com.loopers.infrastructure.refactor.order;

import com.loopers.application.refactor.order.OrderInfo;
import com.loopers.application.refactor.order.OrderQuery;
import com.loopers.application.refactor.order.OrderQueryFacade;
import com.loopers.domain.order.Order;
import org.springframework.data.domain.Page;

public class OrderQueryFacadeImpl implements OrderQueryFacade {
    private final OrderRepository orderRepository;

    public OrderQueryFacadeImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<OrderInfo.DataList> getOrderList(OrderQuery.ListQuery query) {
        return orderRepository.findByUserId(query.userId(), query.pageable())
                .map(OrderInfo.DataList::of)
                ;
    }

    @Override
    public OrderInfo.DataDetail getOrderDetail(OrderQuery.DetailQuery query) {
        Order order = orderRepository.findById(query.orderId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        return OrderInfo.DataDetail.of(order.getId(), order.getLines().getLines());
    }
}
