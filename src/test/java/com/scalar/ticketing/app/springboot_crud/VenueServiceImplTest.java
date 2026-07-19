package com.scalar.ticketing.app.springboot_crud;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scalar.ticketing.app.springboot_crud.application.impl.VenueServiceImpl;
import com.scalar.ticketing.app.springboot_crud.domain.model.Venue;
import com.scalar.ticketing.app.springboot_crud.domain.repository.VenueRepository;
import com.scalar.ticketing.app.springboot_crud.interfaces.dto.VenueRequestDTO;

@ExtendWith(MockitoExtension.class)
class VenueServiceImplTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueServiceImpl venueService;

    private Venue sampleVenue;

    @BeforeEach
    void setUp() {
        sampleVenue = new Venue(
                1L, "Stadium Arena", "Av. Principal 123",
                50000, "Circle",
                LocalDateTime.now(), null
        );
    }

    @Test
    void shouldCreateVenue() {
        when(venueRepository.save(any(Venue.class))).thenAnswer(inv -> {
            Venue v = inv.getArgument(0);
            return new Venue(
                    1L, v.getName(), v.getAddress(),
                    v.getCapacity(), v.getLayout(),
                    LocalDateTime.now(), null
            );
        });

        VenueRequestDTO request = new VenueRequestDTO(
                "Stadium Arena", "Av. Principal 123", 50000, "Circle"
        );

        var response = venueService.createVenue(request);

        assertNotNull(response);
        assertEquals("Stadium Arena", response.name());
        assertEquals(50000, response.capacity());
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    void shouldGetVenueById() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(sampleVenue));

        var result = venueService.getVenueById(1L);

        assertTrue(result.isPresent());
        assertEquals("Stadium Arena", result.get().name());
    }

    @Test
    void shouldReturnEmptyWhenVenueNotFound() {
        when(venueRepository.findById(99L)).thenReturn(Optional.empty());

        var result = venueService.getVenueById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetAllVenues() {
        Venue venue2 = new Venue(
                2L, "Theater", "Calle 45",
                2000, "Rows",
                LocalDateTime.now(), null
        );
        when(venueRepository.findAll()).thenReturn(List.of(sampleVenue, venue2));

        var result = venueService.getAllVenues();

        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateVenue() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(sampleVenue));
        when(venueRepository.save(any(Venue.class))).thenAnswer(inv -> inv.getArgument(0));

        VenueRequestDTO updated = new VenueRequestDTO(
                "New Arena", "New Address 456", 60000, "Square"
        );

        var result = venueService.updateVenue(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("New Arena", result.get().name());
        assertEquals("New Address 456", result.get().address());
        assertEquals(60000, result.get().capacity());
    }

    @Test
    void shouldDeleteVenue() {
        when(venueRepository.existsById(1L)).thenReturn(true);

        boolean deleted = venueService.deleteVenue(1L);

        assertTrue(deleted);
        verify(venueRepository).deleteById(1L);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonexistentVenue() {
        when(venueRepository.existsById(99L)).thenReturn(false);

        boolean deleted = venueService.deleteVenue(99L);

        assertFalse(deleted);
        verify(venueRepository, never()).deleteById(any());
    }
}
