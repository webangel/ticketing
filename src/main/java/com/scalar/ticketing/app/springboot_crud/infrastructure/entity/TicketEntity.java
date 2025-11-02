package com.scalar.ticketing.app.springboot_crud.infrastructure.entity;

import java.util.UUID;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tickets") // Nombre personalizado de tabla
public class TicketEntity {
    @Id
    @Column(name = "ticket_id", nullable = false, updatable = false, length = 36)
    private String ticketId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private Status status; // Usa el enum en lugar de String

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "queue_position")
    private long queuePosition;

    @Column(name = "created_at")
    private long createdAt;

    @Builder
    public TicketEntity(
        String ticketId, 
        EventEntity event,
        UserEntity user, 
        Status status,
        int quantity,
        long queuePosition, 
        long createdAt
        )
         {
        this.ticketId = ticketId;
        this.event = event;
        this.user = user;
        this.status = status;
        this.quantity = quantity;
        this.queuePosition = queuePosition;
        this.createdAt = createdAt;
    }

    // public static TicketEntity fromDomain(Ticket ticket) {
    //     return TicketEntity.builder()
    //             .ticketId(ticket.getTicketId())
    //             .event(EventEntity.fromDomain(ticket.getEvent()))
    //             .user(UserEntity.fromDomain(ticket.getUser()))
    //             .status(ticket.getStatus()) // Usar el enum directamente
    //             .quantity(ticket.getQuantity())
    //             .queuePosition(ticket.getQueuePosition())
    //             .createdAt(ticket.getCreatedAt())
    //             .build();
    // }

    public static TicketEntity fromDomain(Ticket ticket) {
        TicketEntity entity = new TicketEntity();
        entity.setTicketId(ticket.getTicketId());

        // Solo referencia, no convertir el Event completo
        EventEntity eventRef = new EventEntity();
        eventRef.setEventId(ticket.getEvent().getEventId());
        entity.setEvent(eventRef);

        // Igual para user
        UserEntity userRef = new UserEntity();
        userRef.setUserId(ticket.getUser().getUserId());
        entity.setUser(userRef);

        entity.setStatus(ticket.getStatus());
        entity.setQuantity(ticket.getQuantity());
        entity.setQueuePosition(ticket.getQueuePosition());
        entity.setCreatedAt(ticket.getCreatedAt());
        return entity;
    }

    public Ticket toDomain() {
        return Ticket.builder()
                .ticketId(this.ticketId)
                .event(this.event != null ? event.toDomain() : null)
                .user(this.user != null ? user.toDomain() : null)
                .status(this.status) // Usar el enum directamente
                .quantity(this.quantity)
                .queuePosition(this.queuePosition)
                .createdAt(this.createdAt)
                .build();
    }
}
