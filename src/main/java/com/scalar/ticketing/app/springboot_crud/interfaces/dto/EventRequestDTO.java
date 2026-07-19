package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.EventStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record EventRequestDTO(
    @NotBlank(message = "El nombre del evento es obligatorio")
    String name,

    @NotNull(message = "La fecha del evento es obligatoria")
    @FutureOrPresent(message = "La fecha del evento no puede ser anterior a hoy")
    LocalDateTime eventDate,

    @NotNull(message = "El ID del lugar es obligatorio")
    Long venueId,
    
    @PositiveOrZero(message = "El total de asientos debe ser positivo")
    int totalSeats,

    @PositiveOrZero(message = "Los asientos disponibles deben ser positivos")
    int availableSeats,

    EventStatus status,

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    BigDecimal price,

    String image
) {}