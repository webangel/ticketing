package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
public record TicketResponseDTO(
    String ticketId,
    String userId,
    String eventId,
    String status,
    int quantity,
    long queuePosition,
    long createdAt
) {
    public static TicketResponseDTO fromDomain(Ticket ticket) {
        return new TicketResponseDTO(
            ticket.getTicketId(),
            ticket.getUser().getUserId(),
            ticket.getEvent().getEventId(),
            ticket.getStatus().name(),
            ticket.getQuantity(),
            ticket.getQueuePosition(),
            ticket.getCreatedAt()
        );
    }
}