package com.loopers.event.producer.infrastructure;

import com.loopers.event.producer.domain.DeadEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadEventRepository extends JpaRepository<DeadEvent, String> {
}
