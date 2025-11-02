package com.scalar.ticketing.app.springboot_crud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.domain.model.Event;

public interface EventRepository {
    Optional<Event> findById(String eventId);
    Event save(Event event);
    List<Event> findAll();
    boolean existsById(String eventId);
    void deleteById(String eventId);
}