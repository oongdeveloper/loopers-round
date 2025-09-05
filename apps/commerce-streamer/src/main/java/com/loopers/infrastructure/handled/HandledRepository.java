package com.loopers.infrastructure.handled;

import com.loopers.domain.handled.Handled;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HandledRepository extends JpaRepository<Handled, Long> {
    Optional<Handled> findByEventId(String eventId);
}
