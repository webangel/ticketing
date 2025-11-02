package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.VenueEntity;

public interface SpringDataVenueRepository extends JpaRepository<VenueEntity, Long>{
    Optional<VenueEntity> findById(Long venueId);
}
