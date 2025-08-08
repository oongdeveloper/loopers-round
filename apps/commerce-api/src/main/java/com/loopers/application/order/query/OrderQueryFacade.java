package com.loopers.application.order.query;

import org.springframework.data.domain.Page;

public interface OrderQueryFacade {
    Page<OrderInfo.DataList> getOrderList(OrderQuery.Summary query);

    OrderInfo.DataDetail getOrderDetail(OrderQuery.Detail query);
}
