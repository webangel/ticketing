package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.UserRole;

public record  UserResponseDTO (
    String userId,
    String name,
    String email,
    UserRole role
 ) {}
