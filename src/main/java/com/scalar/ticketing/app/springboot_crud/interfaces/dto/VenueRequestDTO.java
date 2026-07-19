package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VenueRequestDTO(
    @NotBlank(message = "El nombre del lugar es obligatorio")
    String name,

    @NotBlank(message = "La dirección es obligatoria")
    String address,

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    int capacity,

    String layout
) {}
