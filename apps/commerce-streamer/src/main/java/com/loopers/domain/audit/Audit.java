package com.loopers.domain.audit;

import com.loopers.domain.BaseEntity;
import com.loopers.event.core.EventType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "audit_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Audit extends BaseEntity {
    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EventType type;

    @Column(name = "payload", nullable = false)
    private String payload;
}
