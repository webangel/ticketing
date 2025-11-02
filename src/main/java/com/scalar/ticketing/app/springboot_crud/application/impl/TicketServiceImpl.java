package com.scalar.ticketing.app.springboot_crud.application.impl;

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
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{
    private static final String DEFAULT_EVENT_ID = "event1";
    private static final int MAX_SEATS = 100;

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    

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
    Ticket ticket = Ticket.builder()
            .ticketId(generateTicketId(userId))
            .event(event)
            .user(user)
            .status(Status.IN_QUEUE)
            .quantity(1)
            .queuePosition(queuePosition)
            .createdAt(System.currentTimeMillis())
            .build();

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

        Event event = eventRepository.findById(DEFAULT_EVENT_ID)
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
                    ticket.getCreatedAt());
            return ticketRepository.save(ticket);
        }

        // Hay entradas disponibles y es tu turno
        Event updatedEvent = event.reduceAvailability();
        eventRepository.save(updatedEvent);

        ticket = new Ticket(
                ticket.getTicketId(),
                ticket.getEvent(),
                ticket.getUser(),
                Status.COMPLETED,
                ticket.getQuantity(),
                ticket.getQueuePosition(),
                ticket.getCreatedAt());

        return ticketRepository.save(ticket);
    }

    @Override
    public String generateTicketId(String userId) {
        return "ticket-" + userId + "-" + System.currentTimeMillis();
    }

}
