package com.scalar.ticketing.app.springboot_crud.application.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scalar.ticketing.app.springboot_crud.application.service.EventService;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.repository.EventRepository;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    
    private final EventRepository eventRepository;
    
    @Override
    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequest) {
        

    Event event = Event.create(
            eventRequest.name(),
            eventRequest.eventDate(),
            eventRequest.venueId(),
            eventRequest.totalSeats(),
            eventRequest.availableSeats(),
            eventRequest.status(),
            eventRequest.price()
        );
        
        Event savedEvent = eventRepository.save(event);
        return toResponseDTO(savedEvent);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<EventResponseDTO> getEventById(String eventId) {
        return eventRepository.findById(eventId)
                .map(this::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public Optional<EventResponseDTO> updateEvent(String eventId, EventRequestDTO eventRequest) {
        return eventRepository.findById(eventId)
                .map(existingEvent -> {
                    Event newData =Event.create(
                        eventRequest.name(),
                        eventRequest.eventDate(),
                        eventRequest.venueId(),
                        eventRequest.totalSeats(),
                        eventRequest.availableSeats(),
                        eventRequest.status(),
                        eventRequest.price()
                    );

                    Event updated = existingEvent.updateWith(newData);
                    Event saved = eventRepository.save(updated);

                    return toResponseDTO(saved);
                });
    }
    
    @Override
    @Transactional
    public boolean deleteEvent(String eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public EventResponseDTO reduceAvailability(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        
        Event updatedEvent = event.reduceAvailability();
        return toResponseDTO(eventRepository.save(updatedEvent));
    }
    
    private EventResponseDTO toResponseDTO(Event event) {
        return new EventResponseDTO(
            event.getEventId(),
            event.getName(),
            event.getTotalSeats(),
            event.getAvailableSeats(),
            event.getEventDate() != null ? event.getEventDate() : LocalDateTime.now()
        );
    }
}