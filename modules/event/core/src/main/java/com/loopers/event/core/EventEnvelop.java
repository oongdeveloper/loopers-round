package com.loopers.event.core;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;


@Getter
@ToString
public class EventEnvelop<T extends EventPayload> {
    private String eventId;
    private ZonedDateTime createdAt;
    private EventType type;
    private T payload;

    public static EventEnvelop<EventPayload> of(String eventId, EventType type, EventPayload payload) {
        EventEnvelop<EventPayload> event = new EventEnvelop<>();
        event.eventId = eventId;
        event.type = type;
        event.createdAt = ZonedDateTime.now();
        event.payload = payload;
        return event;
    }

    public String toJson() {
        return EventSerializer.serialize(this);
    }

    public String payloadToJson() {
        return EventSerializer.serialize(this.payload);
    }

    public static EventEnvelop<EventPayload> fromJson(String json) {
        EventRaw eventRaw = EventSerializer.deserialize(json, EventRaw.class);
        if (eventRaw == null) {
            return null;
        }
        EventEnvelop<EventPayload> event = new EventEnvelop<>();
        event.eventId = eventRaw.getEventId();
        event.createdAt = eventRaw.getCreatedAt();
        event.type = EventType.from(eventRaw.getType());
        event.payload = EventSerializer.deserialize(eventRaw.getPayload(), event.type.getPayloadClass());
        return event;
    }

    public String getTopic(){
        return this.type.getTopic();
    }

    @Getter
    private static class EventRaw {
        private String eventId;
        private String type;
        private ZonedDateTime createdAt;
        private Object payload;
    }
}
