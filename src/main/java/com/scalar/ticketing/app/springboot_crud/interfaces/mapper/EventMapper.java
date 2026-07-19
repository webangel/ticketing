package com.scalar.ticketing.app.springboot_crud.interfaces.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventResponseDTO;

public class EventMapper {

    private EventMapper() {}

    public static EventResponseDTO toResponse(Event event) {
        if (event == null) return null;
        return new EventResponseDTO(
                event.getEventId(),
                event.getName(),
                event.getEventDate(),
                event.getTotalSeats(),
                event.getAvailableSeats(),
                event.getStatus(),
                event.getPrice(),
                event.getImage(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    public static List<EventResponseDTO> toResponseList(List<Event> events) {
        return events.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toList());
    }
}
