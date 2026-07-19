package com.scalar.ticketing.app.springboot_crud.application.service;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;

public interface TicketService {
    Ticket joinQueue(String userId, String eventId);
    Ticket attemptPurchase(String ticketId);
    String generateTicketId(String userId);
    Optional<Ticket> getTicketById(String ticketId);
    List<Ticket> getAllTickets();
    List<Ticket> getTicketsByEventId(String eventId);
    List<Ticket> getTicketsByUserId(String userId);
    Ticket cancelTicket(String ticketId);
}
