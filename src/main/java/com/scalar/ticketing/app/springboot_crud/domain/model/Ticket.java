package com.scalar.ticketing.app.springboot_crud.domain.model;

import java.time.LocalDateTime;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;

public class Ticket {

    private final String ticketId;
    private final Event event;
    private final User user;
    private final Status status;
    private final int quantity;
    private final long queuePosition;
    private final String qtoken;
    private final String pathPdf;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Ticket(String ticketId, Event event, User user, Status status, int quantity,
                  long queuePosition, String qtoken, String pathPdf, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.ticketId = ticketId;
        this.event = event;
        this.user = user;
        this.status = status;
        this.quantity = quantity;
        this.queuePosition = queuePosition;
        this.qtoken = qtoken;
        this.pathPdf = pathPdf;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getTicketId() { return ticketId; }
    public Event getEvent() { return event; }
    public User getUser() { return user; }
    public Status getStatus() { return status; }
    public int getQuantity() { return quantity; }
    public long getQueuePosition() { return queuePosition; }
    public String getQtoken() { return qtoken; }
    public String getPathPdf() { return pathPdf; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
