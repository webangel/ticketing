package com.scalar.ticketing.app.springboot_crud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(String ticketId);
    List<Ticket> findAllByStatusOrderByCreatedAt(Status status);
    long countByStatus(Status status);
    int countByEventIdAndStatus(String eventId, Status inQueue);
}