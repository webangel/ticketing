package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.scalar.ticketing.app.springboot_crud.domain.model.Event;
import com.scalar.ticketing.app.springboot_crud.domain.repository.EventRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.EventEntity;
import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.VenueEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Repository
public class JpaEventRepository implements EventRepository {
    private final SpringDataEventRepository springDataEventRepository;
    private final SpringDataVenueRepository springDataVenueRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public JpaEventRepository(SpringDataEventRepository springDataEventRepository, SpringDataVenueRepository springDataVenueRepository) {
        this.springDataEventRepository = springDataEventRepository;
        this.springDataVenueRepository =  springDataVenueRepository;
    }

    @Override
    public Optional<Event> findById(String eventId) {
        return springDataEventRepository.findById(eventId)
                .map(EventEntity::toDomain);
    }

    @Override
    public Event save(Event event) {
        VenueEntity venueEntity = springDataVenueRepository.findById(event.getVenueId())
            .orElseThrow(() -> new EntityNotFoundException("Venue no encontrado"));

        EventEntity entity = EventEntity.fromDomain(event, venueEntity);
        EventEntity saved = springDataEventRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<Event> findAll() {
        String jpql = "SELECT e FROM EventEntity e";
        return entityManager.createQuery(jpql, EventEntity.class)
                .getResultList()
                .stream()
                .map(EventEntity::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public boolean existsById(String eventId) {
        return springDataEventRepository.existsById(eventId);
    }

    @Override
    public void deleteById(String eventId) {
        springDataEventRepository.deleteById(eventId);
    }
}
