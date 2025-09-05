package com.loopers.event.producer.domain;

import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "dead_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeadEvent {
    @Id
    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EventType type;

    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "published_at")
    private ZonedDateTime publishedAt;

    private DeadEvent(String eventId, ZonedDateTime createdAt, EventType type, String payload, String reason) {
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.type = type;
        this.payload = payload;
        this.reason = reason;
    }

    public void published(){
        this.publishedAt = ZonedDateTime.now();
    }

    public static DeadEvent from(EventEnvelop<?> eventEnvelop, String errMsg){
        return new DeadEvent(
                eventEnvelop.getEventId(),
                eventEnvelop.getCreatedAt(),
                eventEnvelop.getType(),
                eventEnvelop.toJson(),
                errMsg
        );
    }
}
