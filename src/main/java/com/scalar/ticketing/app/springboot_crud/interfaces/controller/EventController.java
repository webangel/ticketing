package com.scalar.ticketing.app.springboot_crud.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalar.ticketing.app.springboot_crud.application.service.EventService;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventResponseDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/events")
public class EventController {
    
    private final EventService eventService;
    
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('EVENT_CREATE')")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO eventRequest) {
        EventResponseDTO response = eventService.createEvent(eventRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{eventId}")
    @PreAuthorize("hasAuthority('EVENT_READ_ONE')")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable String eventId) {
        return eventService.getEventById(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('EVENT_READ_ALL')")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    
    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('EVENT_UPDATE')")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable String eventId,
            @Valid @RequestBody EventRequestDTO eventRequest) {
        return eventService.updateEvent(eventId, eventRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        if (eventService.deleteEvent(eventId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{eventId}/reduce-availability")
    public ResponseEntity<EventResponseDTO> reduceAvailability(@PathVariable String eventId) {
        EventResponseDTO response = eventService.reduceAvailability(eventId);
        return ResponseEntity.ok(response);
    }
}