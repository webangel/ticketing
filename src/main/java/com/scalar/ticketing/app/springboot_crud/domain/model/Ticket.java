package com.scalar.ticketing.app.springboot_crud.domain.model;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Ticket {
    private final String ticketId;
    private final Event event;
    private final User user;
    private final Status status;
    private final int quantity;
    private final long queuePosition;
    private final long createdAt;

    public Ticket(
        String ticketId,
        Event event,
        User user,
        Status status,
        int quantity,
        long queuePosition,
        long createdAt
        ) {
        this.ticketId = ticketId;
        this.event = event;
        this.user = user;
        this.status = status;
        this.quantity = quantity;
        this.queuePosition = queuePosition;
        this.createdAt = createdAt;
    }
   
    
}
