package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;
import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.TicketEntity;

public interface SpringDataTicketRepository extends JpaRepository<TicketEntity, String>{

    List<TicketEntity> findAllByStatusOrderByCreatedAt(Status status);
    long countByStatus(Status status);
    int countByEvent_EventIdAndStatus(String eventId, Status status);
    List<TicketEntity> findByEvent_EventId(String eventId);
    List<TicketEntity> findByUser_UserId(String userId);
    List<TicketEntity> findByUser_UserIdAndStatus(String userId, Status status);
    boolean existsByEvent_EventId(String eventId);
}
