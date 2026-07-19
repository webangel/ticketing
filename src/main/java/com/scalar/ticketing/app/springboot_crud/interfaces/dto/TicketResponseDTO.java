package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import java.time.LocalDateTime;

public record TicketResponseDTO(
    String ticketId,
    String userId,
    String eventId,
    String status,
    int quantity,
    long queuePosition,
    String qtoken,
    String pathPdf,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}