package com.loopers.domain.audit;

import com.loopers.domain.BaseEntity;
import com.loopers.event.core.EventType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Objects;

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

    private Audit(String eventId, ZonedDateTime createdAt, EventType type, String payload) {
        Objects.requireNonNull(eventId);
        Objects.requireNonNull(type);
        Objects.requireNonNull(payload);

        this.eventId = eventId;
        this.createdAt = createdAt;
        this.type = type;
        this.payload = payload;
    }

    public static Audit of(String eventId, ZonedDateTime createdAt, EventType type, String payload){
        return new Audit(eventId, createdAt, type, payload);
    }
}
