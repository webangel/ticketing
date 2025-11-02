package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;
import com.scalar.ticketing.app.springboot_crud.domain.repository.TicketRepository;
import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.TicketEntity;

@Repository
public class JpaTicketRepository implements TicketRepository {
    
    private final SpringDataTicketRepository springDataTicketRepository;

    public JpaTicketRepository(SpringDataTicketRepository springDataTicketRepository) {
        this.springDataTicketRepository = springDataTicketRepository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        try {
            
        TicketEntity entity = TicketEntity.fromDomain(ticket);
        TicketEntity saved = springDataTicketRepository.save(entity);
        return saved.toDomain();
    } catch (Exception e) {
        throw new RuntimeException("Error saving ticket", e);
    }
    }

    @Override
    public Optional<Ticket> findById(String ticketId) {
         try {
             return springDataTicketRepository.findById(ticketId)
                     .map(TicketEntity::toDomain);
        } catch (Exception e) {
            throw new RuntimeException("Error saving ticket", e);
        }
    }

    @Override
    public List<Ticket> findAllByStatusOrderByCreatedAt(Status status) {
         try {
             return springDataTicketRepository.findAllByStatusOrderByCreatedAt(status)
                     .stream()
                     .map(TicketEntity::toDomain)
                     .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error saving ticket", e);
        }
    }

    @Override
    public long countByStatus(Status status) {
         try {
             return springDataTicketRepository.countByStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("Error saving ticket", e);
        }
    }

    @Override
    public int countByEventIdAndStatus(String eventId, Status inQueue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByEventIdAndStatus'");
    }

}
