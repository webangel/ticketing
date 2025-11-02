package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import java.time.LocalDateTime;

public record EventResponseDTO(
    String eventId,
    String name,
    int totalSeats,
    int availableSeats,
    LocalDateTime createdAt
) {}