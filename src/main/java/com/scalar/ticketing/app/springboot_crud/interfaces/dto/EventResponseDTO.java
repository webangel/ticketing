package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.EventStatus;

public record EventResponseDTO(
    String eventId,
    String name,
    LocalDateTime eventDate,
    int totalSeats,
    int availableSeats,
    EventStatus status,
    BigDecimal price,
    String image,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}