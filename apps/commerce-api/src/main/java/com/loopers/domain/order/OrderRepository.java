package com.loopers.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Page<Order> findByUserId(Long userId, Pageable pageable);
    Optional<Order> findByIdWithOrderLines(Long orderId);

//    Page<Order> findByUserId(Long id, Pageable pageable);
    Optional<Order> findById(Long orderId);
}
