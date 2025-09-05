package com.loopers.infrastructure.handled;

import com.loopers.domain.handled.Handled;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HandledRepository extends JpaRepository<Handled, Long> {
}
