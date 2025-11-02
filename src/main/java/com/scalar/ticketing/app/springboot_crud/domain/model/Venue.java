package com.scalar.ticketing.app.springboot_crud.domain.model;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Venue {
    private Long venueId;
    private String name;
    private String address;
    private int capacity;
    private String layout; // Para mapas de asientos
}
