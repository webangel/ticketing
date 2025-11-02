package com.scalar.ticketing.app.springboot_crud.infrastructure.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.EventStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    private String eventId;
    
    @Column(name = "name")
    private String name;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private VenueEntity venue;
    
    @Column(name = "total_seats", nullable = false)
    private int totalSeats;
    
    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TicketEntity> tickets = new ArrayList<>();

    // public void addTicket(TicketEntity ticket) {
    //     this.tickets.add(ticket);
    //     ticket.setEvent(this); // Mantiene la relación en ambos lados
    // }

    //     public static EventEntity fromDomain(Event event, VenueEntity venueEntity) {
    //     return EventEntity.builder()
    //             .eventId(event.getEventId())
    //             .name(event.getName())
    //             .eventDate(event.getEventDate())
    //             .venue(venueEntity)
    //             .totalSeats(event.getTotalSeats())
    //             .availableSeats(event.getAvailableSeats())
    //             .status(event.getStatus())
    //             .price(event.getPrice())
    //             .build();
    // }

    public static EventEntity fromDomain(Event event, VenueEntity venueEntity) {
        EventEntity entity = new EventEntity();
        entity.setEventId(event.getEventId());
        entity.setName(event.getName());
        entity.setEventDate(event.getEventDate());
        entity.setVenue(venueEntity);
        entity.setTotalSeats(event.getTotalSeats());
        entity.setAvailableSeats(event.getAvailableSeats());
        entity.setStatus(event.getStatus());
        entity.setPrice(event.getPrice());
        return entity;
    }

    // public Event toDomain() {
    //     return Event.reconstruct(
    //         this.eventId,
    //         this.name,
    //         this.eventDate,
    //         this.venue.getVenueId(),
    //         this.totalSeats,
    //         this.availableSeats,
    //         this.status,
    //         this.price
    //     );
    // }

    public Event toDomain() {
        return Event.reconstruct(
            this.getEventId(), 
            this.getName(), 
            this.getEventDate(), 
            this.getVenue().getVenueId(), 
            this.getTotalSeats(),  
            this.getAvailableSeats(), 
            this.getStatus(), 
            this.getPrice()
            );
    }
}