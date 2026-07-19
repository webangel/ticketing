package com.scalar.ticketing.app.springboot_crud.application.service;

import java.util.List;
import java.util.Optional;

import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueResponseDTO;

public interface VenueService {
    VenueResponseDTO createVenue(VenueRequestDTO venueRequest);
    Optional<VenueResponseDTO> getVenueById(Long venueId);
    List<VenueResponseDTO> getAllVenues();
    Optional<VenueResponseDTO> updateVenue(Long venueId, VenueRequestDTO venueRequest);
    boolean deleteVenue(Long venueId);
}
