package com.scalar.ticketing.app.springboot_crud;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scalar.ticketing.app.springboot_crud.application.impl.TicketServiceImpl;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.EventStatus;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.UserRole;
import com.scalar.ticketing.app.springboot_crud.domain.repository.EventRepository;
import com.scalar.ticketing.app.springboot_crud.domain.repository.TicketRepository;
import com.scalar.ticketing.app.springboot_crud.domain.repository.UserRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.service.EmailService;
import com.scalar.ticketing.app.springboot_crud.infrastructure.service.PdfService;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PdfService pdfService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User sampleUser;
    private Event sampleEvent;

    @BeforeEach
    void setUp() {
        sampleUser = new User(
                "user-1", "test@mail.com", "Test User",
                "pass123", UserRole.CUSTOMER,
                LocalDateTime.now(), null
        );

        sampleEvent = Event.reconstruct(
                "evt-1", "Concierto",
                LocalDateTime.now().plusDays(7),
                1L, 100, 100,
                EventStatus.PUBLISHED,
                new BigDecimal("50.00"),
                null,
                LocalDateTime.now(), null
        );
    }

    @Test
    void shouldJoinQueue() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(sampleUser));
        when(eventRepository.findById("evt-1")).thenReturn(Optional.of(sampleEvent));
        when(ticketRepository.countByEventIdAndStatus("evt-1", Status.IN_QUEUE)).thenReturn(2);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket ticket = ticketService.joinQueue("user-1", "evt-1");

        assertNotNull(ticket);
        assertEquals(Status.IN_QUEUE, ticket.getStatus());
        assertEquals(3, ticket.getQueuePosition());
        assertEquals(1, ticket.getQuantity());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void shouldRejectJoinWhenUserNotFound() {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ticketService.joinQueue("unknown", "evt-1"));
    }

    @Test
    void shouldRejectJoinWhenEventNotFound() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(sampleUser));
        when(eventRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ticketService.joinQueue("user-1", "unknown"));
    }

    @Test
    void shouldCompletePurchaseWhenSeatsAvailable() throws Exception {
        Ticket ticket = new Ticket(
                "tkt-1", sampleEvent, sampleUser,
                Status.IN_QUEUE, 1, 1,
                null, null,
                LocalDateTime.now(), null
        );

        when(ticketRepository.findById("tkt-1")).thenReturn(Optional.of(ticket));
        when(eventRepository.findById("evt-1")).thenReturn(Optional.of(sampleEvent));
        when(ticketRepository.countByStatus(Status.IN_QUEUE)).thenReturn(5L);
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pdfService.generateTicketPdf(any(Ticket.class))).thenReturn("./pdf-tickets/tkt-1.pdf");

        Ticket result = ticketService.attemptPurchase("tkt-1");

        assertEquals(Status.COMPLETED, result.getStatus());
        assertNotNull(result.getPathPdf());
        assertEquals("tkt-1", result.getQtoken());
    }

    @Test
    void shouldRejectPurchaseWhenNoSeats() {
        Event soldOut = Event.reconstruct(
                "evt-2", "Sold Out", LocalDateTime.now().plusDays(1), 1L,
                50, 0, EventStatus.PUBLISHED, new BigDecimal("30.00"),
                null, LocalDateTime.now(), LocalDateTime.now()
        );

        Ticket ticket = new Ticket(
                "tkt-2", soldOut, sampleUser,
                Status.IN_QUEUE, 1, 1,
                null, null,
                LocalDateTime.now(), null
        );

        when(ticketRepository.findById("tkt-2")).thenReturn(Optional.of(ticket));
        when(eventRepository.findById("evt-2")).thenReturn(Optional.of(soldOut));
        when(ticketRepository.countByStatus(Status.IN_QUEUE)).thenReturn(3L);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket result = ticketService.attemptPurchase("tkt-2");

        assertEquals(Status.REJECTED, result.getStatus());
    }

    @Test
    void shouldRejectPurchaseWhenNotInQueue() {
        Ticket ticket = new Ticket(
                "tkt-3", sampleEvent, sampleUser,
                Status.COMPLETED, 1, 1,
                null, null,
                LocalDateTime.now(), null
        );

        when(ticketRepository.findById("tkt-3")).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class,
                () -> ticketService.attemptPurchase("tkt-3"));
    }

    @Test
    void shouldCancelTicket() {
        Ticket ticket = new Ticket(
                "tkt-4", sampleEvent, sampleUser,
                Status.IN_QUEUE, 1, 2,
                null, null,
                LocalDateTime.now(), null
        );

        when(ticketRepository.findById("tkt-4")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Ticket result = ticketService.cancelTicket("tkt-4");

        assertEquals(Status.CANCELLED, result.getStatus());
    }

    @Test
    void shouldNotCancelTicketWhenNotInQueue() {
        Ticket ticket = new Ticket(
                "tkt-5", sampleEvent, sampleUser,
                Status.COMPLETED, 1, 1,
                null, null,
                LocalDateTime.now(), null
        );

        when(ticketRepository.findById("tkt-5")).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class,
                () -> ticketService.cancelTicket("tkt-5"));
    }

    @Test
    void shouldGetTicketById() {
        Ticket ticket = new Ticket(
                "tkt-6", sampleEvent, sampleUser,
                Status.IN_QUEUE, 1, 1,
                null, null,
                LocalDateTime.now(), null
        );
        when(ticketRepository.findById("tkt-6")).thenReturn(Optional.of(ticket));

        Optional<Ticket> result = ticketService.getTicketById("tkt-6");

        assertTrue(result.isPresent());
        assertEquals("tkt-6", result.get().getTicketId());
    }

    @Test
    void shouldGetTicketsByEventId() {
        Ticket ticket = new Ticket(
                "tkt-7", sampleEvent, sampleUser,
                Status.IN_QUEUE, 1, 1,
                null, null,
                LocalDateTime.now(), null
        );
        when(ticketRepository.findByEventId("evt-1")).thenReturn(List.of(ticket));

        List<Ticket> result = ticketService.getTicketsByEventId("evt-1");

        assertEquals(1, result.size());
        assertEquals("evt-1", result.get(0).getEvent().getEventId());
    }

    @Test
    void shouldGetTicketsByUserId() {
        Ticket ticket = new Ticket(
                "tkt-8", sampleEvent, sampleUser,
                Status.COMPLETED, 1, 1,
                null, null,
                LocalDateTime.now(), null
        );
        when(ticketRepository.findByUserId("user-1")).thenReturn(List.of(ticket));

        List<Ticket> result = ticketService.getTicketsByUserId("user-1");

        assertEquals(1, result.size());
        assertEquals("user-1", result.get(0).getUser().getUserId());
    }

    @Test
    void shouldGenerateTicketId() {
        String id = ticketService.generateTicketId("user-1");

        assertTrue(id.startsWith("ticket-user-1-"));
    }
}
