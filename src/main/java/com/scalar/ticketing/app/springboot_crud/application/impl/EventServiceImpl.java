package com.scalar.ticketing.app.springboot_crud.application.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scalar.ticketing.app.springboot_crud.application.service.EventService;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.repository.EventRepository;
import com.scalar.ticketing.app.springboot_crud.domain.repository.TicketRepository;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventResponseDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.mapper.EventMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

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
                eventRequest.price(),
                eventRequest.image());

        Event savedEvent = eventRepository.save(event);
        return EventMapper.toResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventResponseDTO> getEventById(String eventId) {
        return eventRepository.findById(eventId)
                .map(EventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        return EventMapper.toResponseList(eventRepository.findAll());
    }

    @Override
    @Transactional
    public Optional<EventResponseDTO> updateEvent(String eventId, EventRequestDTO eventRequest) {
        return eventRepository.findById(eventId)
                .map(existingEvent -> {
                    Event newData = Event.create(
                            eventRequest.name(),
                            eventRequest.eventDate(),
                            eventRequest.venueId(),
                            eventRequest.totalSeats(),
                            eventRequest.availableSeats(),
                            eventRequest.status(),
                            eventRequest.price(),
                            eventRequest.image());

                    Event updated = existingEvent.updateWith(newData);
                    Event saved = eventRepository.save(updated);

                    return EventMapper.toResponse(saved);
                });
    }

    @Override
    @Transactional
    public boolean deleteEvent(String eventId) {
        if (ticketRepository.existsByEventId(eventId)) {
            throw new IllegalStateException("No se puede eliminar un evento que tiene tickets asociados. Los eventos se conservan como histórico.");
        }
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
        return EventMapper.toResponse(eventRepository.save(updatedEvent));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasTickets(String eventId) {
        return ticketRepository.existsByEventId(eventId);
    }
}