package com.loopers.infrastructure.order.query;

import com.loopers.application.order.query.OrderResult;
import com.loopers.application.order.query.OrderQuery;
import com.loopers.application.order.query.OrderQueryFacade;
import com.loopers.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class OrderQueryFacadeImpl implements OrderQueryFacade {
    private final OrderQueryRepository orderQueryRepository;

    public OrderQueryFacadeImpl(OrderQueryRepository orderQueryRepository) {
        this.orderQueryRepository = orderQueryRepository;
    }

    @Override
    public Page<OrderResult.DataList> getOrderList(OrderQuery.Summary query) {
        return orderQueryRepository.findByUserId(query.userId(), query.pageable())
                .map(OrderResult.DataList::of)
                ;
    }

    @Override
    public OrderResult.DataDetail getOrderDetail(OrderQuery.Detail query) {
        Order order = orderQueryRepository.findById(query.orderId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        return OrderResult.DataDetail.of(order.getId(), order.getLines().getLines());
    }
}
