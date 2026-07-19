package com.scalar.ticketing.app.springboot_crud.interfaces.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.scalar.ticketing.app.springboot_crud.domain.model.Venue;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueResponseDTO;

public class VenueMapper {

    private VenueMapper() {}

    public static VenueResponseDTO toResponse(Venue venue) {
        if (venue == null) return null;
        return new VenueResponseDTO(
                venue.getVenueId(),
                venue.getName(),
                venue.getAddress(),
                venue.getCapacity(),
                venue.getLayout(),
                venue.getCreatedAt(),
                venue.getUpdatedAt()
        );
    }

    public static List<VenueResponseDTO> toResponseList(List<Venue> venues) {
        return venues.stream()
                .map(VenueMapper::toResponse)
                .collect(Collectors.toList());
    }
}
