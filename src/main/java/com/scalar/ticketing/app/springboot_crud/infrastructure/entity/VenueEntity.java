package com.scalar.ticketing.app.springboot_crud.infrastructure.entity;

import com.scalar.ticketing.app.springboot_crud.domain.model.Venue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venues")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id")
    private Long venueId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int capacity;

    private String layout; // Para mapas de asientos


    public Venue toDomain() {
        return new Venue(venueId, name, address, capacity, layout);
    }

    public static VenueEntity fromDomain(Venue venue) {
        if (venue == null) return null;
        return VenueEntity.builder()
                .venueId(venue.getVenueId()) // asegúrate que tu VO o entidad tenga este campo si aplica
                .name(venue.getName())
                .address(venue.getAddress())
                .capacity(venue.getCapacity())
                .layout(venue.getLayout())
                .build();
    }
}