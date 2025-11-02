package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.scalar.ticketing.app.springboot_crud.domain.model.enums.Status;
import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.TicketEntity;

public interface SpringDataTicketRepository extends JpaRepository<TicketEntity, String>{

    List<TicketEntity> findAllByStatusOrderByCreatedAt(Status status);
    long countByStatus(Status status);
}
