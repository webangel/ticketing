package com.scalar.ticketing.app.springboot_crud.application.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scalar.ticketing.app.springboot_crud.application.service.TicketService;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.domain.model.User;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;
import com.scalar.ticketing.app.springboot_crud.domain.repository.EventRepository;
import com.scalar.ticketing.app.springboot_crud.domain.repository.TicketRepository;
import com.scalar.ticketing.app.springboot_crud.domain.repository.UserRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.service.EmailService;
import com.scalar.ticketing.app.springboot_crud.infrastructure.service.PdfService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
    private static final String DEFAULT_EVENT_ID = "event1";
    private static final int MAX_SEATS = 100;

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PdfService pdfService;
    private final EmailService emailService;

    

// public TicketServiceImpl(TicketRepository ticketRepository, EventRepository eventRepository,
//             UserRepository userRepository) {
//         this.ticketRepository = ticketRepository;
//         this.eventRepository = eventRepository;
//         this.userRepository = userRepository;
//     }

@Override
@Transactional
public Ticket joinQueue(String userId, String eventId) {

    // Buscar usuario
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Buscar evento
    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

    // 2 Calcular la posición actual en la cola
    long queuePosition = ticketRepository.countByEventIdAndStatus(eventId, Status.IN_QUEUE) + 1;

    // 3 Crear el ticket con todos los datos necesarios
    Ticket ticket = new Ticket(
            generateTicketId(userId),
            event,
            user,
            Status.IN_QUEUE,
            1,
            queuePosition,
            null,
            null,
            LocalDateTime.now(),
            null
    );

    // 4 Guardar el ticket
    return ticketRepository.save(ticket);
}

    @Override
    @Transactional
    public Ticket attemptPurchase(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado"));

        if (ticket.getStatus() != Status.IN_QUEUE) {
            throw new IllegalStateException("El ticket no está en cola");
        }

        Event event = eventRepository.findById(ticket.getEvent().getEventId())
                .orElseThrow(() -> new IllegalStateException("Evento no encontrado"));

        long usersInQueueBeforeMe = ticketRepository.countByStatus(Status.IN_QUEUE) - 
                                  ticket.getQueuePosition();

        if (usersInQueueBeforeMe < 0 || event.getAvailableSeats() <= 0) {
            ticket = new Ticket(
                    ticket.getTicketId(),
                    ticket.getEvent(),
                    ticket.getUser(),
                    Status.REJECTED,
                    ticket.getQuantity(),
                    ticket.getQueuePosition(),
                    ticket.getQtoken(),
                    ticket.getPathPdf(),
                    ticket.getCreatedAt(),
                    LocalDateTime.now()
            );
            return ticketRepository.save(ticket);
        }

        // Hay entradas disponibles y es tu turno
        Event updatedEvent = event.reduceAvailability();
        eventRepository.save(updatedEvent);

        String pdfPath = null;
        try {
            pdfPath = pdfService.generateTicketPdf(ticket);
            emailService.sendTicketPdfEmail(ticket, pdfPath);
        } catch (Exception e) {
            logger.error("Error al generar PDF o enviar email para ticket {}: {}", ticket.getTicketId(), e.getMessage());
        }

        ticket = new Ticket(
                ticket.getTicketId(),
                ticket.getEvent(),
                ticket.getUser(),
                Status.COMPLETED,
                ticket.getQuantity(),
                ticket.getQueuePosition(),
                ticket.getTicketId(),
                pdfPath,
                ticket.getCreatedAt(),
                LocalDateTime.now()
        );

        return ticketRepository.save(ticket);
    }

    @Override
    public String generateTicketId(String userId) {
        return "ticket-" + userId + "-" + System.currentTimeMillis();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> getTicketById(String ticketId) {
        return ticketRepository.findById(ticketId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllByStatusOrderByCreatedAt(Status.IN_QUEUE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByEventId(String eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByUserId(String userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Ticket cancelTicket(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado"));

        if (ticket.getStatus() != Status.IN_QUEUE) {
            throw new IllegalStateException("Solo se pueden cancelar tickets en cola");
        }

        Ticket cancelled = new Ticket(
                ticket.getTicketId(),
                ticket.getEvent(),
                ticket.getUser(),
                Status.CANCELLED,
                ticket.getQuantity(),
                ticket.getQueuePosition(),
                ticket.getQtoken(),
                ticket.getPathPdf(),
                ticket.getCreatedAt(),
                LocalDateTime.now()
        );

        return ticketRepository.save(cancelled);
    }
}
