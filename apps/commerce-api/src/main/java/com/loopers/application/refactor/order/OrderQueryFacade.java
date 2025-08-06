package com.loopers.application.refactor.order;

import org.springframework.data.domain.Page;

public interface OrderQueryFacade {
    Page<OrderInfo.DataList> getOrderList(OrderQuery.ListQuery query);

    OrderInfo.DataDetail getOrderDetail(OrderQuery.DetailQuery query);
}
