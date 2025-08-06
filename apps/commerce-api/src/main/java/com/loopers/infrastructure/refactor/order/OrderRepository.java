package com.loopers.infrastructure.refactor.order;

import com.loopers.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long id, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderLines.lines WHERE o.id = :orderId AND o.deletedAt IS NULL")
    Optional<Order> findById(@Param("orderId") Long orderId);
}
