package com.scalar.ticketing.app.springboot_crud.interfaces.dto;

import com.scalar.ticketing.app.springboot_crud.domain.model.enums.UserRole;

public record UserRegistrationDTO(String name, String email, String password, UserRole role) {
    public UserRegistrationDTO {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    public String maskedEmail() {
        return email.replaceAll("(?<=.).(?=.*@)", "*");
    }
}