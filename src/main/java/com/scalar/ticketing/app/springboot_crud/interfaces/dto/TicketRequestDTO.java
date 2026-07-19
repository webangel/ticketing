package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketRequestDTO(
    @NotBlank(message = "El ID del usuario es obligatorio")
    String userId,

    @NotBlank(message = "El ID del evento es obligatorio")
    String eventId
) {}