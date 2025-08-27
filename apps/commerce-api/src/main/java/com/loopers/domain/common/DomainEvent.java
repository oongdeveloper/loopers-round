package com.loopers.domain.common;

import java.time.ZonedDateTime;

public interface DomainEvent {

    default ZonedDateTime createdAt() {
        return ZonedDateTime.now();
    }
}
