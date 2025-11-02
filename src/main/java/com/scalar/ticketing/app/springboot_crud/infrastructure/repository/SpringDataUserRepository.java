package com.scalar.ticketing.app.springboot_crud.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.UserEntity;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, String>{
    Optional<UserEntity> findByEmail(String email);
}
