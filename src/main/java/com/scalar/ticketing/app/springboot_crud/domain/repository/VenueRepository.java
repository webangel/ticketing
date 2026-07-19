package com.scalar.ticketing.app.springboot_crud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.domain.model.Venue;

public interface VenueRepository {
    Optional<Venue> findById(Long venueId);
    List<Venue> findAll();
    Venue save(Venue venue);
    boolean existsById(Long venueId);
    void deleteById(Long venueId);
}
