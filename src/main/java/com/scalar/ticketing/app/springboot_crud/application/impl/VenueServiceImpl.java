package com.scalar.ticketing.app.springboot_crud.application.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scalar.ticketing.app.springboot_crud.application.service.VenueService;
import com.scalar.ticketing.app.springboot_crud.domain.model.Venue;
import com.scalar.ticketing.app.springboot_crud.domain.repository.VenueRepository;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueRequestDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueResponseDTO;
import com.scalar.ticketing.app.springboot_crud.interfaces.mapper.VenueMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    @Transactional
    public VenueResponseDTO createVenue(VenueRequestDTO venueRequest) {
        Venue venue = new Venue(
                null,
                venueRequest.name(),
                venueRequest.address(),
                venueRequest.capacity(),
                venueRequest.layout(),
                null,
                null
        );

        Venue savedVenue = venueRepository.save(venue);
        return VenueMapper.toResponse(savedVenue);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VenueResponseDTO> getVenueById(Long venueId) {
        return venueRepository.findById(venueId)
                .map(VenueMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueResponseDTO> getAllVenues() {
        return VenueMapper.toResponseList(venueRepository.findAll());
    }

    @Override
    @Transactional
    public Optional<VenueResponseDTO> updateVenue(Long venueId, VenueRequestDTO venueRequest) {
        return venueRepository.findById(venueId)
                .map(existingVenue -> {
                    Venue updatedVenue = new Venue(
                            venueId,
                            venueRequest.name(),
                            venueRequest.address(),
                            venueRequest.capacity(),
                            venueRequest.layout(),
                            existingVenue.getCreatedAt(),
                            null
                    );
                    Venue saved = venueRepository.save(updatedVenue);
                    return VenueMapper.toResponse(saved);
                });
    }

    @Override
    @Transactional
    public boolean deleteVenue(Long venueId) {
        if (venueRepository.existsById(venueId)) {
            venueRepository.deleteById(venueId);
            return true;
        }
        return false;
    }
}
