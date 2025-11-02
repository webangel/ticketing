package com.scalar.ticketing.app.springboot_crud.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.EventStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Event {


    private final String eventId;
    private final String name;
    private final LocalDateTime eventDate;
    private final Long venueId;
    private final int totalSeats;
    private final int availableSeats;
    private final EventStatus status;
    private final BigDecimal price;

    // 🔒 Constructor privado: evita instanciación directa
    private Event(String eventId, String name, LocalDateTime eventDate, Long venueId,
                  int totalSeats, int availableSeats, EventStatus status, BigDecimal price) {
        this.eventId = eventId;
        this.name = name;
        this.eventDate = eventDate;
        this.venueId = venueId;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.status = status;
        this.price = price;
    }

    // 🏗️ Método de fábrica estático con validaciones
    public static Event create(String name, LocalDateTime eventDate, Long venueId,
                               int totalSeats, int availableSeats, EventStatus status, BigDecimal price) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del evento es obligatorio");
        }

        if (eventDate == null) {
            throw new IllegalArgumentException("La fecha del evento no puede ser nula");
        }

        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento no puede ser anterior a hoy");
        }

        if (totalSeats <= 0) {
            throw new IllegalArgumentException("El número total de asientos debe ser mayor que 0");
        }

        if (availableSeats < 0 || availableSeats > totalSeats) {
            throw new IllegalArgumentException("Los asientos disponibles deben estar entre 0 y el total de asientos");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio del evento debe ser positivo");
        }

        if (status == null) {
            status = EventStatus.DRAFT;
        }

        return new Event(null, name, eventDate, venueId, totalSeats, availableSeats, status, price);
    }

    // 🔁 Método de actualización inmutable
    public Event updateWith(Event other) {
        return new Event(
            this.eventId,
            other.name != null ? other.name : this.name,
            other.eventDate != null ? other.eventDate : this.eventDate,
            other.venueId != null ? other.venueId : this.venueId,
            other.totalSeats != 0 ? other.totalSeats : this.totalSeats,
            other.availableSeats != 0 ? other.availableSeats : this.availableSeats,
            other.status != null ? other.status : this.status,
            other.price != null ? other.price : this.price
        );
    }

    // 🎟️ Lógica de negocio: reducir disponibilidad
    public Event reduceAvailability() {
        if (this.availableSeats <= 0) {
            throw new IllegalStateException("No hay entradas disponibles para el evento: " + this.name);
        }
        return new Event(
            this.eventId,
            this.name,
            this.eventDate,
            this.venueId,
            this.totalSeats,
            this.availableSeats - 1,
            this.status,
            this.price
        );
    }

    public static Event reconstruct(String eventId, String name, LocalDateTime eventDate, Long venueId,
                                    int totalSeats, int availableSeats, EventStatus status, BigDecimal price) {
        return new Event(eventId, name, eventDate, venueId, totalSeats, availableSeats, status, price);
    }
}
