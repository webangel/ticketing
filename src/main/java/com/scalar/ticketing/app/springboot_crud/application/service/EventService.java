package com.scalar.ticketing.app.springboot_crud.application.service;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventResponseDTO;

public interface EventService {
    EventResponseDTO createEvent(EventRequestDTO eventRequest);
    Optional<EventResponseDTO> getEventById(String eventId);
    List<EventResponseDTO> getAllEvents();
    Optional<EventResponseDTO> updateEvent(String eventId, EventRequestDTO eventRequest);
    boolean deleteEvent(String eventId);
    EventResponseDTO reduceAvailability(String eventId);
}
