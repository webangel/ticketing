package com.scalar.ticketing.app.springboot_crud;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scalar.ticketing.app.springboot_crud.application.impl.EventServiceImpl;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.EventStatus;
import com.scalar.ticketing.app.springboot_crud.domain.repository.EventRepository;
import com.scalar.ticketing.app.springboot_crud.domain.repository.TicketRepository;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.EventResponseDTO;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event sampleEvent;
    private EventRequestDTO sampleRequest;

    @BeforeEach
    void setUp() {
        sampleEvent = Event.create(
                "Concierto Rock",
                LocalDateTime.now().plusDays(7),
                1L,
                200,
                200,
                EventStatus.PUBLISHED,
                new BigDecimal("50.00"),
                "http://img.com/rock.jpg"
        );

        sampleRequest = new EventRequestDTO(
                "Concierto Rock",
                LocalDateTime.now().plusDays(7),
                1L,
                200,
                200,
                EventStatus.PUBLISHED,
                new BigDecimal("50.00"),
                "http://img.com/rock.jpg"
        );
    }

    @Test
    void shouldCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> {
            Event e = inv.getArgument(0);
            return Event.reconstruct(
                    "evt-1", e.getName(), e.getEventDate(), e.getVenueId(),
                    e.getTotalSeats(), e.getAvailableSeats(), e.getStatus(),
                    e.getPrice(), e.getImage(), LocalDateTime.now(), LocalDateTime.now()
            );
        });

        EventResponseDTO response = eventService.createEvent(sampleRequest);

        assertNotNull(response);
        assertEquals("Concierto Rock", response.name());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldGetEventById() {
        when(eventRepository.findById("evt-1")).thenReturn(Optional.of(sampleEvent));

        Optional<EventResponseDTO> result = eventService.getEventById("evt-1");

        assertTrue(result.isPresent());
        assertEquals("Concierto Rock", result.get().name());
    }

    @Test
    void shouldReturnEmptyWhenEventNotFound() {
        when(eventRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<EventResponseDTO> result = eventService.getEventById("nonexistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetAllEvents() {
        Event event2 = Event.create(
                "Festival Jazz",
                LocalDateTime.now().plusDays(14),
                2L,
                100,
                100,
                EventStatus.DRAFT,
                new BigDecimal("75.00"),
                null
        );
        when(eventRepository.findAll()).thenReturn(List.of(sampleEvent, event2));

        List<EventResponseDTO> result = eventService.getAllEvents();

        assertEquals(2, result.size());
    }

    @Test
    void shouldDeleteEventWhenNoTickets() {
        when(ticketRepository.existsByEventId("evt-1")).thenReturn(false);
        when(eventRepository.existsById("evt-1")).thenReturn(true);

        boolean deleted = eventService.deleteEvent("evt-1");

        assertTrue(deleted);
        verify(eventRepository).deleteById("evt-1");
    }

    @Test
    void shouldNotDeleteEventWhenHasTickets() {
        when(ticketRepository.existsByEventId("evt-1")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> eventService.deleteEvent("evt-1"));
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    void shouldReduceAvailability() {
        when(eventRepository.findById("evt-1")).thenReturn(Optional.of(sampleEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        EventResponseDTO result = eventService.reduceAvailability("evt-1");

        assertNotNull(result);
        assertEquals(199, result.availableSeats());
    }

    @Test
    void shouldThrowWhenReducingZeroAvailability() {
        Event soldOut = Event.reconstruct(
                "evt-2", "Sold Out", LocalDateTime.now().plusDays(1), 1L,
                100, 0, EventStatus.PUBLISHED, new BigDecimal("30.00"),
                null, LocalDateTime.now(), LocalDateTime.now()
        );
        when(eventRepository.findById("evt-2")).thenReturn(Optional.of(soldOut));

        assertThrows(IllegalStateException.class,
                () -> eventService.reduceAvailability("evt-2"));
    }

    @Test
    void shouldUpdateEvent() {
        when(eventRepository.findById("evt-1")).thenReturn(Optional.of(sampleEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        EventRequestDTO updatedRequest = new EventRequestDTO(
                "Concierto Rock 2.0",
                LocalDateTime.now().plusDays(10),
                1L,
                300,
                250,
                EventStatus.PUBLISHED,
                new BigDecimal("60.00"),
                "http://img.com/rock2.jpg"
        );

        Optional<EventResponseDTO> result = eventService.updateEvent("evt-1", updatedRequest);

        assertTrue(result.isPresent());
        assertEquals("Concierto Rock 2.0", result.get().name());
        assertEquals(0, new BigDecimal("60.00").compareTo(result.get().price()));
    }

    @Test
    void shouldReturnFalseWhenDeletingNonexistentEvent() {
        when(ticketRepository.existsByEventId("nonexistent")).thenReturn(false);
        when(eventRepository.existsById("nonexistent")).thenReturn(false);

        boolean deleted = eventService.deleteEvent("nonexistent");

        assertFalse(deleted);
    }
}
