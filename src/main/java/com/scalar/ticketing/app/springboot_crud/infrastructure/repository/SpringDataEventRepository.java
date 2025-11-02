package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.EventEntity;

@Repository
public interface SpringDataEventRepository extends JpaRepository<EventEntity, String>{

}
