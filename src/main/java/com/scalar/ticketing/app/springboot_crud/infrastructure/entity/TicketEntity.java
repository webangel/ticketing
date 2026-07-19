package com.scalar.ticketing.app.springboot_crud.infrastructure.entity;

import java.time.LocalDateTime;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tickets")
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
    private Status status;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "queue_position")
    private long queuePosition;

    @Column(name = "qtoken")
    private String qtoken;

    @Column(name = "path_pdf")
    private String pathPdf;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Builder
    public TicketEntity(String ticketId, EventEntity event, UserEntity user, Status status,
                        int quantity, long queuePosition, String qtoken, String pathPdf) {
        this.ticketId = ticketId;
        this.event = event;
        this.user = user;
        this.status = status;
        this.quantity = quantity;
        this.queuePosition = queuePosition;
        this.qtoken = qtoken;
        this.pathPdf = pathPdf;
    }

    public static TicketEntity fromDomain(Ticket ticket) {
        TicketEntity entity = new TicketEntity();
        entity.setTicketId(ticket.getTicketId());

        EventEntity eventRef = new EventEntity();
        eventRef.setEventId(ticket.getEvent().getEventId());
        entity.setEvent(eventRef);

        UserEntity userRef = new UserEntity();
        userRef.setUserId(ticket.getUser().getUserId());
        entity.setUser(userRef);

        entity.setStatus(ticket.getStatus());
        entity.setQuantity(ticket.getQuantity());
        entity.setQueuePosition(ticket.getQueuePosition());
        entity.setQtoken(ticket.getQtoken());
        entity.setPathPdf(ticket.getPathPdf());
        return entity;
    }

    public Ticket toDomain() {
        return new Ticket(
                this.ticketId,
                this.event != null ? event.toDomain() : null,
                this.user != null ? user.toDomain() : null,
                this.status,
                this.quantity,
                this.queuePosition,
                this.qtoken,
                this.pathPdf,
                this.createdAt,
                this.updatedAt
        );
    }
}
