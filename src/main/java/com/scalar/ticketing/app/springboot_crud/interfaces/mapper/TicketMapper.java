package com.scalar.ticketing.app.springboot_crud.interfaces.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.TicketResponseDTO;

public class TicketMapper {

    private TicketMapper() {}

    public static TicketResponseDTO toResponse(Ticket ticket) {
        if (ticket == null || ticket.getUser() == null || ticket.getEvent() == null) {
            return null;
        }
        return new TicketResponseDTO(
                ticket.getTicketId(),
                ticket.getUser().getUserId(),
                ticket.getEvent().getEventId(),
                ticket.getStatus().name(),
                ticket.getQuantity(),
                ticket.getQueuePosition(),
                ticket.getQtoken(),
                ticket.getPathPdf(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    public static List<TicketResponseDTO> toResponseList(List<Ticket> tickets) {
        return tickets.stream()
                .map(TicketMapper::toResponse)
                .collect(Collectors.toList());
    }
}
