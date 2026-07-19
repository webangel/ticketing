package com.scalar.ticketing.app.springboot_crud.interfaces.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalar.ticketing.app.springboot_crud.application.service.VenueService;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    @PreAuthorize("hasAuthority('VENUE_CREATE')")
    public ResponseEntity<VenueResponseDTO> createVenue(@Valid @RequestBody VenueRequestDTO venueRequest) {
        VenueResponseDTO response = venueService.createVenue(venueRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{venueId}")
    @PreAuthorize("hasAuthority('VENUE_READ_ONE')")
    public ResponseEntity<VenueResponseDTO> getVenueById(@PathVariable Long venueId) {
        return venueService.getVenueById(venueId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VENUE_READ_ALL')")
    public ResponseEntity<List<VenueResponseDTO>> getAllVenues() {
        List<VenueResponseDTO> venues = venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }

    @PutMapping("/{venueId}")
    @PreAuthorize("hasAuthority('VENUE_UPDATE')")
    public ResponseEntity<VenueResponseDTO> updateVenue(
            @PathVariable Long venueId,
            @Valid @RequestBody VenueRequestDTO venueRequest) {
        return venueService.updateVenue(venueId, venueRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{venueId}")
    @PreAuthorize("hasAuthority('VENUE_DELETE')")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long venueId) {
        if (venueService.deleteVenue(venueId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
