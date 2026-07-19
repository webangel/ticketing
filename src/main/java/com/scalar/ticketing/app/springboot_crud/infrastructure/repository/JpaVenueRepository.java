package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.scalar.ticketing.app.springboot_crud.domain.model.Venue;
import com.scalar.ticketing.app.springboot_crud.domain.repository.VenueRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.VenueEntity;

@Repository
public class JpaVenueRepository implements VenueRepository {

    private final SpringDataVenueRepository springDataVenueRepository;

    public JpaVenueRepository(SpringDataVenueRepository springDataVenueRepository) {
        this.springDataVenueRepository = springDataVenueRepository;
    }

    @Override
    public Optional<Venue> findById(Long venueId) {
        return springDataVenueRepository.findById(venueId)
                .map(VenueEntity::toDomain);
    }

    @Override
    public List<Venue> findAll() {
        return springDataVenueRepository.findAll()
                .stream()
                .map(VenueEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Venue save(Venue venue) {
        VenueEntity entity = VenueEntity.fromDomain(venue);
        VenueEntity saved = springDataVenueRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public boolean existsById(Long venueId) {
        return springDataVenueRepository.existsById(venueId);
    }

    @Override
    public void deleteById(Long venueId) {
        springDataVenueRepository.deleteById(venueId);
    }
}
