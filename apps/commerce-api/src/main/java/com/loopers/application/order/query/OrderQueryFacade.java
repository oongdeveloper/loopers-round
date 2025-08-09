package com.loopers.application.order.query;

import org.springframework.data.domain.Page;

public interface OrderQueryFacade {
    Page<OrderResult.DataList> getOrderList(OrderQuery.Summary query);

    OrderResult.DataDetail getOrderDetail(OrderQuery.Detail query);
}
