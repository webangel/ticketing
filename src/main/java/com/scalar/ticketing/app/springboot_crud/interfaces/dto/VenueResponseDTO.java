package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import java.time.LocalDateTime;

public record VenueResponseDTO(
    Long venueId,
    String name,
    String address,
    int capacity,
    String layout,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
